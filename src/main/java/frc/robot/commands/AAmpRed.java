package frc.robot.commands;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Drive_Train;

public class AAmpRed extends Command {

    private Drive_Train _drive;
    private double startTime;
    private final long tolerance = 12;
    private final IntegerTopic numTargetsTopic;
    private final IntegerSubscriber numTargetsSub;
    private NetworkTableInstance ninst;


    public AAmpRed(Drive_Train drive, NetworkTableInstance inst) {
        _drive = drive;
        ninst = inst;
        // Use addRequirements() here to declare subsystem dependencies.
        numTargetsTopic = ninst.getIntegerTopic("/datatable/num_targets_detected");
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
        while(Timer.getFPGATimestamp()-startTime<=1.0) {
            _drive.drive(0.5,0);
        }
        if(numTargetsSub.get() == 0) {
            _drive.drive(0,0.3); 
        }
        else {
            new AutonomousDrive(_drive,ninst);
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
