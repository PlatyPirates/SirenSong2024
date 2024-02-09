// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.OperatorConstants;
//import frc.robot.commands.Autos;
import frc.robot.subsystems.LeftClaw;
import frc.robot.subsystems.RightClaw;
import frc.robot.subsystems.Drive_Train;
import frc.robot.commands.ArcadeDrive;
import edu.wpi.first.wpilibj.Joystick;
//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.Command;
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
  private final Joystick _driver = new Joystick(0);
  private final Joystick _operator = new Joystick(1);

  private final LeftClaw _leftClaw = new LeftClaw();
  private final RightClaw _rightClaw = new RightClaw();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    _drive_Train.setDefaultCommand(new ArcadeDrive(_drive_Train, _driver));
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
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    //configure when have commands
    // new JoystickButton(_operator, JoystickConstants.BUMPER_RIGHT)
    //   .onTrue(new InstantCommand(() -> _bootyIntake.setState(BootyState.CubeIntake)))
    //   .onFalse(new InstantCommand(() -> _bootyIntake.setState(BootyState.CubeHold))); 
    // new JoystickButton(_operator, JoystickConstants.BUMPER_LEFT)
    //   .onTrue(new InstantCommand(() -> _bootyIntake.setState(BootyState.ConeIntake)))
    //   .onFalse(new InstantCommand(() -> _bootyIntake.setState(BootyState.ConeHold)));
    
    new JoystickButton(_operator, JoystickConstants.BUMPER_RIGHT).whileTrue(new RunCommand(_rightClaw::rightClawUp, _rightClaw));
    new JoystickButton(_operator, JoystickConstants.RIGHT_TRIGGER).whileTrue(new RunCommand(_rightClaw::rightClawDown,_rightClaw));
    new JoystickButton(_operator, JoystickConstants.BUMPER_LEFT).whileTrue(new RunCommand(_leftClaw::leftClawUp, _leftClaw));
    new JoystickButton(_operator, JoystickConstants.LEFT_TRIGGER).whileTrue(new RunCommand(_leftClaw::leftClawDown, _leftClaw));
      //new JoystickButton(_operator, JoystickConstants.Y).whileTrue(new RunCommand(_fourBarArms::armOut, _fourBarArms));
    // new JoystickButton(_operator, JoystickConstants.A).whileTrue(new RunCommand(_fourBarArms::armIn, _fourBarArms));
    // new JoystickButton(_operator, JoystickConstants.X).whileTrue(new RunCommand(_intakePivot::pivotUp, _intakePivot));
    // new JoystickButton(_operator, JoystickConstants.B).whileTrue(new RunCommand(_intakePivot::pivotDown, _intakePivot));
    
    // new Trigger(m_dualClaw::exampleCondition)
    //     .onTrue(new LeftClaw(m_dualClaw));

    // // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // // cancelling on release.
    // m_driverController.b().whileTrue(m_dualClaw.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(dualClaw);
  }
  */
}
