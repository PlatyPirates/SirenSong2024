package frc.robot.commands;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Drive_Train;

public class AutonomousDrive extends Command {

    private Drive_Train _drive;
    private DoubleTopic _centerTagX;
    private IntegerTopic _centerImageX;
    private final DoubleSubscriber tagSub;
    private final IntegerSubscriber imageSub;
    private final long tolerance = 12;

    public AutonomousDrive(Drive_Train drive, DoubleTopic centerTagX, IntegerTopic centerImageX) {
        // Use addRequirements() here to declare subsystem dependencies.
        _drive = drive;
        _centerTagX = centerTagX;
        _centerImageX = centerImageX;
        tagSub = _centerTagX.subscribe(-1);
        imageSub = _centerImageX.subscribe(-1);
        addRequirements(drive);
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize(){}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute(){
        double centerTagX = tagSub.get();
        long centerImageX = imageSub.get();

        if(Math.abs(centerImageX-(long)centerTagX) <= tolerance){
            _drive.drive(0.2, 0);
        } else if ((centerImageX-(long)centerTagX) > tolerance){
            _drive.drive(0, -0.1);
        } else if(((long)centerTagX-centerImageX) > tolerance){
            _drive.drive(0,0.1);
        }
    }
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
