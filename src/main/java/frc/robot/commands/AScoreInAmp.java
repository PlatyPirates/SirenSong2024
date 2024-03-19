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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;
import java.lang.Thread;

public class AScoreInAmp extends Command {

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
    private boolean isBlue;
    private String alliance;
    private double currentTime;
    private double stateStartTime;
    private double elapsedStateTime;

    enum State {
        MOVE_TO_AUTO_LINE,
        AUTOROTATE,
        TAXI_NO_APRILTAG,
        ADJUST_POSITION,
        GO_TO_APRILTAG,
        SCOOCH_TO_SCORE,
        SCORE,
        BACK_UP,
        TAXI_WITH_APRILTAG,
        LOOK_LEFT,
        LOOK_RIGHT,
        RECENTER,
        SCOOCH_TO_FIND,
        END
    }

    private static State state = State.MOVE_TO_AUTO_LINE;

    public AScoreInAmp(Drive_Train drive, Intake_Bar inputIntakeBar, Intake_Belt inputIntakeBelt, Trap_Rollers inputTrapRollers, NetworkTableInstance inst) {
        _drive = drive;
        intakeBar = inputIntakeBar;
        intakeBelt = inputIntakeBelt;
        trapRollers = inputTrapRollers;
        ninst = inst;
        alliance = "issue";
        centerTagXTopic = inst.getDoubleTopic("/datatable/center_of_amp_X");
        centerImageXTopic = inst.getIntegerTopic("/datatable/center_of_image_X");
        numTargetsTopic = inst.getIntegerTopic("/datatable/num_targets_detected");
        tagSub = centerTagXTopic.subscribe(-1);
        imageSub = centerImageXTopic.subscribe(-1);
        numTargetsSub = numTargetsTopic.subscribe(-1);


        addRequirements(drive, inputIntakeBar, inputIntakeBelt, inputTrapRollers);
    }

    public void setAlliance(String inputAlliance) {
        alliance = inputAlliance;
        //isBlue = alliance.equals("blue");
        isBlue = (DriverStation.getAlliance().toString().equals("Blue"));
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize(){
        startTime = Timer.getFPGATimestamp();
        changeState(State.MOVE_TO_AUTO_LINE);
        isBlue = DriverStation.getAlliance().toString().equals("Blue");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double centerTagX = tagSub.get();
        long centerImageX = imageSub.get();
        long numTargets = numTargetsSub.get();
        double currentTime = Timer.getFPGATimestamp();
        elapsedStateTime = currentTime - stateStartTime;
        double encoder = Math.abs(_drive.getLeftEncoder());

        SmartDashboard.putNumber("State Time", elapsedStateTime);
        SmartDashboard.putNumber("Auto Time", (startTime - currentTime));
        SmartDashboard.putString("Autonomous State", state.toString());

        switch(state) {
            case MOVE_TO_AUTO_LINE:
                _drive.drive(0.5, 0.0);

                if (encoder >= 1.0) {
                    changeState(State.AUTOROTATE);
                }
                break;

            case AUTOROTATE:
                if (isBlue) {
                    _drive.drive(0,-0.31);
                }
                else {
                    _drive.drive(0,0.31);
                }

                if (encoder >= 1.7) {
                    /* encoder value >= one rotation */
                    changeState(State.TAXI_NO_APRILTAG);
                }
                else if (numTargets > 0) {
                    /* num targets is greater than 0 */
                    changeState(State.ADJUST_POSITION);
                }
                break;

            case TAXI_NO_APRILTAG:
                _drive.drive(0.4,0.0);
                if (encoder >= 1.0) {
                    changeState(State.END);
                }
                break;

            case ADJUST_POSITION:
                if (centerTagX < (long)centerImageX && !(Math.abs(centerImageX-(long)centerTagX) <= tolerance)) {
                    /* if the tag is left of center */
                    _drive.drive(0, -0.4);
                }
                else if((long)centerTagX > centerImageX && !(Math.abs(centerImageX-(long)centerTagX) <= tolerance)) {
                    /* if the tag is right of center */
                    _drive.drive(0, 0.4);
                }

                if (numTargets <= 0) {
                    /* number of targets is less than or equal to 0 */
                    changeState(State.LOOK_LEFT);
                }
                else if (Math.abs(centerImageX-(long)centerTagX) <= tolerance) {
                    /* target is centered */
                    changeState(State.GO_TO_APRILTAG);
                }
                break;

            case GO_TO_APRILTAG:
                if (Math.abs(centerImageX-(long)centerTagX) <= tolerance) {
                    _drive.drive(0.5, 0.0);
                }
                else if (centerTagX < (long)centerImageX) {
                    /* if the tag is left of center */
                    _drive.drive(0, -0.4);
                }
                else if((long)centerTagX > centerImageX) {
                    /* if the tag is right of center */
                    _drive.drive(0, 0.4);
                }

                if (numTargets <= 0) {
                    /* num targets is less than or equal to 0 */
                    changeState(State.SCOOCH_TO_SCORE);
                }
                break;

            case SCOOCH_TO_SCORE:
                _drive.drive(0.5, 0.0);

                if (encoder >= 1.0) {
                    changeState(State.SCORE);
                }
                break;

            case SCORE:
                intakeBar.IntakeBarIn();
                intakeBelt.IntakeBeltIn();

                if (elapsedStateTime >= 1.0) {
                    /* timer has elapsed */
                    changeState(State.BACK_UP);
                }
                break;

            case BACK_UP:
                if (isBlue) {
                    _drive.drive(-0.2,0.2);
                }
                else {
                    _drive.drive(-0.2,-0.2);
                }

                if (encoder >= 0.5) {
                    changeState(State.TAXI_WITH_APRILTAG);
                }
                break;

            case TAXI_WITH_APRILTAG:
                _drive.drive(-0.5,0.0);

                if (encoder >= 1.5) {
                    changeState(State.END);
                }
                break;

            case LOOK_LEFT:
                _drive.drive(0.0,-0.3);

                if (numTargets > 0) {
                    /* num targets is greater than 0 */
                    changeState(State.ADJUST_POSITION);
                }
                else if(encoder >= 0.5) {
                    changeState(State.LOOK_RIGHT);
                }
                break;

            case LOOK_RIGHT:
                _drive.drive(0.0,0.3);

                if (numTargets > 0) {
                    /* num targets is greater than 0 */
                    changeState(State.ADJUST_POSITION);
                }
                else if (encoder >= 1.0) {
                    changeState(State.RECENTER);
                }
                break;

            case RECENTER:
                _drive.drive(0.0,-0.3);

                if (numTargets > 0) {
                    /* num targets is greater than 0 */
                    changeState(State.ADJUST_POSITION);
                }
                else if (encoder >= 0.25) {
                    changeState(State.SCOOCH_TO_FIND);
                }
                break;

            case SCOOCH_TO_FIND:
                _drive.drive(0.4,0.0);

                if (numTargets > 0) {
                    /* num targets is greater than 0 */
                    changeState(State.ADJUST_POSITION);
                }
                else if (encoder > 0.4) {
                    changeState(State.LOOK_LEFT);
                }
                break;

            case END:
                //give some indication that it (thinks) it's done
                SmartDashboard.putBoolean("Status", true);
                end = true;
                break;
        }
    }

    public void changeState(State inputState){
        stateStartTime = Timer.getFPGATimestamp();
        _drive.encoderReset();
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
