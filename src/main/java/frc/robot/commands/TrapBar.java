// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.Intake_Bar;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Limit_Switch;

public class TrapBar extends Command {
  private final Intake_Bar _intakeBar;
  private final Limit_Switch _limitSwitch;
  /** Creates a new SetUpTrap. */
  public TrapBar(Intake_Bar intakeBar, Limit_Switch limitSwitch) {
    _intakeBar = intakeBar;
    _limitSwitch = limitSwitch;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(_intakeBar);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    _intakeBar.IntakeBarOut();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    _intakeBar.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return _limitSwitch.isPressed();
  }
}
