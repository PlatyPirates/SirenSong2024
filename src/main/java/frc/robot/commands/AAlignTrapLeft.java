package frc.robot.commands;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.*;

public class AAlignTrapLeft extends Command {

    private Drive_Train _drive;
    private Intake_Bar intakeBar;
    private Intake_Belt intakeBelt;
    private Trap_Rollers trapRollers;
    private RightClaw rightClaw;

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
    private boolean isRed;
    private double currentTime;
    private double stateStartTime;
    private double elapsedStateTime;

    enum State {
        TAXI,
        LOAD,
        DEPLOY,
        SEARCH,
        ALIGN,
        SCOOT,
        RAISE_CLAW,
        END
    }

    private static State state = State.TAXI;

    public AAlignTrapLeft(Drive_Train drive, Intake_Belt inputIntakeBelt, Intake_Bar inputIntakeBar, Trap_Rollers inputTrapRollers, RightClaw inputRightClaw, NetworkTableInstance inst) {
        _drive = drive;
        intakeBelt = inputIntakeBelt;
        intakeBar = inputIntakeBar;
        trapRollers = inputTrapRollers;
        rightClaw = inputRightClaw;

        ninst = inst;
        centerTagXTopic = inst.getDoubleTopic("/datatable/center_of_amp_X");
        centerImageXTopic = inst.getIntegerTopic("/datatable/center_of_image_X");
        numTargetsTopic = inst.getIntegerTopic("/datatable/num_targets_detected");
        tagSub = centerTagXTopic.subscribe(-1);
        imageSub = centerImageXTopic.subscribe(-1);
        numTargetsSub = numTargetsTopic.subscribe(-1);


        addRequirements(drive, intakeBelt, intakeBar, trapRollers, rightClaw);
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        changeState(State.TAXI);
        _drive.encoderReset();
        isRed = !DriverStation.getAlliance().toString().equals("Optional[Blue]");
        SmartDashboard.putBoolean("isRed", isRed);
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

        double encL = Math.abs(_drive.getLeftEncoder());
        double encR = Math.abs(_drive.getRightEncoder());
 
        SmartDashboard.putNumber("State Time", elapsedStateTime);
        SmartDashboard.putNumber("Auto Time", (startTime - currentTime));
        SmartDashboard.putString("Autonomous State", state.toString());
  
        switch(state) {
            case TAXI:
                _drive.drive(-0.5,0.0);
                
                if(encL >= 1.942 && encR >= 1.944){
                    changeState(State.LOAD);    
                }
                break;
            case LOAD:
                intakeBelt.TrapBelt();
                intakeBar.TrapBar();
                trapRollers.TrapScoreRollers();

                if(!frc.robot.subsystems.Limit_Switch.limitSwitch.get()){
                    changeState(State.DEPLOY);
                }
                break;

            case DEPLOY:
                rightClaw.ClawDown();
                
                if(elapsedStateTime >= 0.3){
                    changeState(State.SEARCH);
                }

                break;
                
            case SEARCH:
                _drive.drive(0.0,-0.3);
                if((encR >= 0.28) || numTargets > 0){
                    if(numTargets > 0){
                        changeState(State.ALIGN);
                    } else {
                        changeState(State.SCOOT);
                    }
                }
                
                break;

            case ALIGN:
                if (centerTagX < (long)centerImageX && !(Math.abs(centerImageX-(long)centerTagX) <= tolerance)) {
                    _drive.drive(0, -0.2);
                }
                else if((long)centerTagX > centerImageX && !(Math.abs(centerImageX-(long)centerTagX) <= tolerance)) {
                    _drive.drive(0, 0.2);
                }

                else if ((Math.abs(centerImageX - (long)centerTagX) <= tolerance) || numTargets <= 0) {
                    changeState(State.SCOOT);
                }
                break;

            case SCOOT:
                _drive.drive(-0.3,0.0);
                
                if(encL >= 0.65 && encR >= 0.65){
                    changeState(State.RAISE_CLAW);
                }
                break;
            
            case RAISE_CLAW:
                rightClaw.ClawUp();
                if(elapsedStateTime >= 0.3){
                    changeState(State.END);
                }
                break;

            case END:
                SmartDashboard.putBoolean("Status", true);
                end = true;
                break;
        }
    }

    public void changeState(State inputState){
        stateStartTime = Timer.getFPGATimestamp();
        _drive.encoderReset();
        state = inputState;
        rightClaw.stop();
    }

    @Override
    public void end(boolean interrupted) {
        _drive.stop();
    }

    @Override
    public boolean isFinished() {
        return end;
    }

}
