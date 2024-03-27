// package frc.robot.commands;
// import edu.wpi.first.networktables.DoubleSubscriber;
// import edu.wpi.first.networktables.DoubleTopic;
// import edu.wpi.first.networktables.GenericEntry;
// import edu.wpi.first.networktables.IntegerSubscriber;
// import edu.wpi.first.networktables.IntegerTopic;
// import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.util.sendable.Sendable;
// import edu.wpi.first.util.sendable.SendableBuilder;
// import edu.wpi.first.util.sendable.SendableRegistry;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.Encoder;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import frc.robot.RobotContainer;
// import frc.robot.subsystems.*;
// import java.lang.Thread;

// public class AScoreInAmp extends Command {

//     private Drive_Train _drive;
//     private double startTime;
//     private final long tolerance = 12;
//     private final DoubleTopic centerTagXTopic;
//     private final IntegerTopic centerImageXTopic;
//     private final IntegerTopic numTargetsTopic;
//     private final DoubleSubscriber tagSub;
//     private final IntegerSubscriber imageSub;
//     private final IntegerSubscriber numTargetsSub;
//     private NetworkTableInstance ninst;
//     private boolean end = false;
//     private Intake_Bar intakeBar;
//     private Intake_Belt intakeBelt;
//     private Trap_Rollers trapRollers;
//     private boolean isBlue;
//     private String alliance;
//     private double currentTime;
//     private double stateStartTime;
//     private double elapsedStateTime;

//     enum State {
//         TAXI,
//         SEARCH,
//         ALIGN,
//         SCOOT,
//         END
//     }

//     private static State state = State.MOVE_TO_AUTO_LINE;

//     public AScoreInAmp(Drive_Train drive, Intake_Bar inputIntakeBar, Intake_Belt inputIntakeBelt, Trap_Rollers inputTrapRollers, NetworkTableInstance inst) {
//         _drive = drive;
//         intakeBar = inputIntakeBar;
//         intakeBelt = inputIntakeBelt;
//         trapRollers = inputTrapRollers;
//         ninst = inst;
//         alliance = "issue";
//         centerTagXTopic = inst.getDoubleTopic("/datatable/center_of_amp_X");
//         centerImageXTopic = inst.getIntegerTopic("/datatable/center_of_image_X");
//         numTargetsTopic = inst.getIntegerTopic("/datatable/num_targets_detected");
//         tagSub = centerTagXTopic.subscribe(-1);
//         imageSub = centerImageXTopic.subscribe(-1);
//         numTargetsSub = numTargetsTopic.subscribe(-1);


//         addRequirements(drive, inputIntakeBar, inputIntakeBelt, inputTrapRollers);
//     }

//     public void setAlliance(String inputAlliance) {
//         alliance = inputAlliance;
//         //isBlue = alliance.equals("blue");
//         isBlue = (DriverStation.getAlliance().toString().equals("Blue"));
//     }
//     // Called when the command is initially scheduled.
//     @Override
//     public void initialize(){
//         startTime = Timer.getFPGATimestamp();
//         changeState(State.MOVE_TO_AUTO_LINE);
//         _drive.encoderReset();
//         isBlue = !DriverStation.getAlliance().toString().equals("Optional[Blue]");
//         SmartDashboard.putBoolean("IsBlue", isBlue);
//         SmartDashboard.putBoolean("Auto Status", false);
//     }

//     // Called every time the scheduler runs while the command is scheduled.
//     @Override
//     public void execute() {
//         double centerTagX = tagSub.get();
//         long centerImageX = imageSub.get();
//         long numTargets = numTargetsSub.get();
//         double currentTime = Timer.getFPGATimestamp();
//         elapsedStateTime = currentTime - stateStartTime;

//         double encL = Math.abs(_drive.getLeftEncoder());
//         double encR = Math.abs(_drive.getRightEncoder());
 
//         SmartDashboard.putNumber("State Time", elapsedStateTime);
//         SmartDashboard.putNumber("Auto Time", (startTime - currentTime));
//         SmartDashboard.putString("Autonomous State", state.toString());
  
//         switch(state) {
//             case TAXI:
//                 _drive.drive(0.5,0.0);
//                 if(encL >= 2.142 && encR >= 2.144){
//                     changeState(State.SEARCH);    
//                 }
//                 break;
//             case SEARCH:
//                 _drive.drive(0.0,0.3);
//                 if(encL >= 0.280 && encR >= 0.249 && numTargets > 0){
//                     if(numTargets > 0){
//                         changeState(State.ALIGN);
//                     } else {
//                         changeState(State.SCOOT);
//                     }
//                 }
//                 break;
//             case ALIGN:







//             case SCOOT:
//                 _drive.drive(0.3,0.0);





//             case END:
//                 //give some indication that it (thinks) it's done
//                 SmartDashboard.putBoolean("Status", true);
//                 end = true;
//                 break;
//         }
//     }

//     public void changeState(State inputState){
//         stateStartTime = Timer.getFPGATimestamp();
//         _drive.encoderReset();
//         state = inputState;
//     }


//     // Called once the command ends or is interrupted.
//     @Override
//     public void end(boolean interrupted) {
//         _drive.stop();
//         intakeBar.stop();
//         intakeBelt.stop();
//     }

//     // Returns true when the command should end.
//     @Override
//     public boolean isFinished() {
//         return end;
//     }

// }
