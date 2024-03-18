package frc.robot.commands;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;
import java.lang.Thread;

public class LEDState extends Command {

    private Drive_Train _drive;
    private double startTime;
    private final long tolerance = 12;
    private final DoubleTopic centerTagXTopic;
    private final IntegerTopic centerImageXTopic;
    private final IntegerTopic numTargetsTopic;
    private final DoubleSubscriber tagSub;
    private final IntegerSubscriber imageSub;
    private final IntegerSubscriber numTargetsSub;
    private NetworkTableInstance ninst;
    private boolean end = false;
    private Intake_Bar intakeBar;
    private Intake_Belt intakeBelt;
    private Trap_Rollers trapRollers;
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;
    private boolean isBlue;
    private String alliance;
    private double currentTime;
    private double stateStartTime;
    private double elapsedStateTime;
    private Color baseColor;
    private Color flashColor;

    enum State {
        DISCONNECTED,
        FLASH_FLASHCOLOR,
        FLASH_BASECOLOR,
        CONNECTED
    }

    private static State state = State.DISCONNECTED;

    public LEDState(NetworkTableInstance inst, AddressableLED inputLed, AddressableLEDBuffer inputLedBuffer) {
        ninst = inst;
        alliance = "issue";
        centerTagXTopic = inst.getDoubleTopic("/datatable/center_of_amp_X");
        centerImageXTopic = inst.getIntegerTopic("/datatable/center_of_image_X");
        numTargetsTopic = inst.getIntegerTopic("/datatable/num_targets_detected");
        tagSub = centerTagXTopic.subscribe(-1);
        imageSub = centerImageXTopic.subscribe(-1);
        numTargetsSub = numTargetsTopic.subscribe(-1);
        led = inputLed;
        ledBuffer = inputLedBuffer;
    }

    public void setAlliance(String inputAlliance) {
        alliance = inputAlliance;
        isBlue = (DriverStation.getAlliance().toString().equals("Blue"));
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        changeState(State.DISCONNECTED);
        SmartDashboard.putBoolean("Auto Status", false);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double centerTagX = tagSub.get();
        long centerImageX = imageSub.get();
        long numTargets = numTargetsSub.get();
        double currentTime = Timer.getFPGATimestamp();
        elapsedStateTime = currentTime - stateStartTime;

        switch(state) {
            case DISCONNECTED:
                baseColor = new Color(255,255,255); //WHITE
                flashColor = new Color(255,0,0); //RED
                changeState(State.FLASH_BASECOLOR);
                break;
            case FLASH_BASECOLOR:
                for (int i = 0; i<ledBuffer.getLength(); i++){
                    ledBuffer.setRGB(i,(int)(baseColor.red*255),(int)(baseColor.green*255),(int)(baseColor.blue*255));
                }
                if(elapsedStateTime >= 0.1) {
                    changeState(State.FLASH_FLASHCOLOR);
                }
                break;
            case FLASH_FLASHCOLOR:
                for (int i = 0; i<ledBuffer.getLength(); i++){
                    ledBuffer.setRGB(i,(int)(flashColor.red*255),(int)(flashColor.green*255),(int)(flashColor.blue*255));
                }
                if(elapsedStateTime >= 0.1) {
                    changeState(State.FLASH_BASECOLOR);
                }
                break;
        }
    }

    public void setColor(Color inputColor){
        baseColor = inputColor;
    }

    public void changeState(State inputState){
        stateStartTime = Timer.getFPGATimestamp();
        state = inputState;
    }


    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        _drive.stop();
        intakeBar.stop();
        intakeBelt.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return end;
    }

}
