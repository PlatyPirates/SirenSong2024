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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Drive_Train;

public class AAmpRed extends Command {

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
    private boolean autoRotate = true;


    public AAmpRed(Drive_Train drive, NetworkTableInstance inst) {
        _drive = drive;
        ninst = inst;
        // Use addRequirements() here to declare subsystem dependencies.
        centerTagXTopic = inst.getDoubleTopic("/datatable/center_of_amp_X");
        centerImageXTopic = inst.getIntegerTopic("/datatable/center_of_image_X");
        numTargetsTopic = inst.getIntegerTopic("/datatable/num_targets_detected");
        tagSub = centerTagXTopic.subscribe(-1);
        imageSub = centerImageXTopic.subscribe(-1);
        numTargetsSub = numTargetsTopic.subscribe(-1);

        addRequirements(drive);
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute(){
        double centerTagX = tagSub.get();
        long centerImageX = imageSub.get();
        long numTargets = numTargetsSub.get(); 

        while(Timer.getFPGATimestamp()-startTime<=1.0) {
            _drive.drive(0.5,0);
        }
        if(numTargets == 0 && autoRotate) {
            _drive.drive(0,0.3); 
        }
        else {
            if (numTargets <= 0 || centerTagX <= 0) { // if there is no tag detected
                // Robot does not move
                _drive.drive(0,0);
            } else if (Math.abs(centerImageX-(long)centerTagX) <= tolerance) { // if there is a tag in the center of video
                // Robot drives forward
                long starttime = System.currentTimeMillis();
                autoRotate = false;
                while(System.currentTimeMillis()-starttime<=250){
                    SmartDashboard.putNumber("Start time",starttime);
                    SmartDashboard.putNumber("Current time",System.currentTimeMillis());
                    _drive.drive(0.22, 0);
                }
                SmartDashboard.putNumber("Start time",starttime);
                SmartDashboard.putNumber("Current time",System.currentTimeMillis());
                starttime = System.currentTimeMillis();
                if(numTargets == 0) {
                    while(System.currentTimeMillis()-starttime <= 500 && numTargets == 0) {
                        _drive.drive(0, 0.3);
                    }
                    starttime = System.currentTimeMillis();
                    while(System.currentTimeMillis()-starttime <= 500 && numTargets == 0) {
                        _drive.drive(0, -0.3);
                    }
                }
            } else if (centerTagX < (long)centerImageX) { // if the tag is left of center
                // Robot drives to the left
                while(System.currentTimeMillis()-startTime<=100){
                    _drive.drive(0, -0.4);
                }
            } else if((long)centerTagX > centerImageX) { // if the tag is right of center
                // Robot drives to the right
                while(System.currentTimeMillis()-startTime<=100){
                    _drive.drive(0, 0.4);
                }
            } else {
                System.out.println("Undefined autonomous drive behavior");
            }
        }
    }
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        _drive.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
