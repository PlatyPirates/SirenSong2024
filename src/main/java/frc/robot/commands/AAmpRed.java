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
// import edu.wpi.first.wpilibj.Encoder;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import frc.robot.subsystems.*;
// import java.lang.Thread;

// public class AAmpRed extends Command {

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
//     private boolean autoRotate = true;
//     private boolean end =false;
//     private Intake_Bar intakeBar;
//     private Intake_Belt intakeBelt;
//     private Trap_Rollers trapRollers;

//     public AAmpRed(Drive_Train drive, Intake_Bar inputIntakeBar, Intake_Belt inputIntakeBelt, Trap_Rollers inputTrapRollers, NetworkTableInstance inst) {
//         _drive = drive;
//         intakeBar = inputIntakeBar;
//         intakeBelt = inputIntakeBelt;
//         trapRollers = inputTrapRollers;
//         ninst = inst;
//         // Use addRequirements() here to declare subsystem dependencies.
//         centerTagXTopic = inst.getDoubleTopic("/datatable/center_of_amp_X");
//         centerImageXTopic = inst.getIntegerTopic("/datatable/center_of_image_X");
//         numTargetsTopic = inst.getIntegerTopic("/datatable/num_targets_detected");
//         tagSub = centerTagXTopic.subscribe(-1);
//         imageSub = centerImageXTopic.subscribe(-1);
//         numTargetsSub = numTargetsTopic.subscribe(-1);

//         addRequirements(drive, inputIntakeBar, inputIntakeBelt, inputTrapRollers);
//     }
//     // Called when the command is initially scheduled.
//     @Override
//     public void initialize(){
//         startTime = Timer.getFPGATimestamp();
//         _drive.encoderReset();
//     }

//     // Called every time the scheduler runs while the command is scheduled.
//     @Override
//     public void execute() {
//         double centerTagX = tagSub.get();
//         long centerImageX = imageSub.get();
//         long numTargets = numTargetsSub.get();

//         if (timeElapsed <= 1.0) {
//             _drive.drive(0.5,0);
//         }

//         //Rotate until an amp target is visible
//         else if (numTargets == 0 && autoRotate) {
//             if (timeElapsed < 2)
//             {
//                 if (_drive.getLeftEncoder() < 1.7) {
//                     _drive.drive(0,0.31); //NOTE Increase this number if it's struggling to turn, lower it if it's not recognizing the AprilTags (not turning, slow turn)
//                 }
//                 else {
//                     _drive.drive(0.4,0);
//                 }
//             }
//             else
//             {
//                 autoRotate = false;
//                 if (timeElapsed < 4) {
//                     intakeBar.IntakeBarIn();
//                     intakeBelt.IntakeBeltIn();
//                 }
//                 else {
//                     _drive.encoderReset();
//                     if (_drive.getLeftEncoder() < 0.525){
//                         _drive.drive(0.1,-0.3);
//                     }
//                     else if (_drive.getLeftEncoder() < 0.5) {
//                         _drive.drive(0.4,0.0);
//                     }
//                 }
//             }
//         }

//         //When a target is visible, center it in the robot's field of vision and move towards it
//         else {
//             if (numTargets <= 0 || centerTagX <= 0) { // if there is no tag detected
//                 // Robot does not move
//                 _drive.drive(0,0);
//             } else if (Math.abs(centerImageX-(long)centerTagX) <= tolerance) { // if there is a tag in the center of video
//                 // Robot drives forward
//                 autoRotate = false;
//                 if (timeElapsed <= 600){
//                     _drive.drive(0.37, 0);
//                 }
//             } else if (centerTagX < (long)centerImageX) { // if the tag is left of center
//                 // Robot drives to the left
//                     _drive.drive(0, -0.4);
//             } else if((long)centerTagX > centerImageX) { // if the tag is right of center
//                 // Robot drives to the right
//                     _drive.drive(0, 0.4);
//             } else {
//                 System.out.println("Undefined autonomous drive behavior");
//             }
//         }
//     }
//     // Called once the command ends or is interrupted.
//     @Override
//     public void end(boolean interrupted) {
//         _drive.stop();
//     }

//     // Returns true when the command should end.
//     @Override
//     public boolean isFinished() {
//         return end;
//     }

// }
