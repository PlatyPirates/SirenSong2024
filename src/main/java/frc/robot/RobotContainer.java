// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.OperatorConstants;
//import frc.robot.commands.Autos;
import frc.robot.subsystems.LeftClaw;
import frc.robot.subsystems.RightClaw;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drive_Train;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.AutonomousDrive;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.event.EventLoop;
//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drive_Train _drive_Train = new Drive_Train();
  //private final Joystick _driver = new Joystick(0);
  //private final Joystick _operator = new Joystick(1);
  private final CommandXboxController _driver = new CommandXboxController(0);
  private final CommandXboxController _operator = new CommandXboxController(1);

  private final LeftClaw _leftClaw = new LeftClaw();
  private final Claw _rightClaw = new RightClaw();
  private DoubleTopic centerTagX;
  private IntegerTopic centerImageX;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    setUpNetworkTable();
    _drive_Train.setDefaultCommand(
      Commands.run(
        () ->
            _drive_Train.drive(
                -_driver.getLeftY(), -_driver.getRightX()),
        _drive_Train));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    _operator
      .leftBumper()
      .whileTrue(new RunCommand(_leftClaw::ClawUp, _leftClaw));
      //.whileTrue(Commands.runOnce(() -> _leftClaw.ClawUp(), _leftClaw));
    
    _operator
      .rightBumper()
      .whileTrue(new RunCommand(_rightClaw::ClawUp, _rightClaw));
      //.whileTrue(Commands.runOnce(() -> _rightClaw.ClawUp(), _rightClaw));
    
    _operator
      .leftTrigger()
      .whileTrue(new RunCommand(_leftClaw::ClawDown, _leftClaw));
      //.whileTrue(Commands.runOnce(() -> _leftClaw.ClawDown(), _leftClaw));
    
    _operator
      .rightTrigger()
      .whileTrue(new RunCommand(_rightClaw::ClawDown, _rightClaw));
      //.whileTrue(Commands.runOnce(() -> _rightClaw.ClawDown(), _rightClaw));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
  */ 
  private void setUpNetworkTable(){
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    centerTagX = inst.getDoubleTopic("/datatable/center_of_amp_X");
    centerImageX = inst.getIntegerTopic("/datatable/center_of_image_X");
  }
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return new AutonomousDrive(_drive_Train, centerTagX, centerImageX);
  }
}
