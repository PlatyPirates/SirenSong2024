// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.Intake_Belt;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Limit_Switch;

public class TrapBelt extends Command {
  private final Intake_Belt _intakeBelt;
  private final Limit_Switch _limitSwitch;
  /** Creates a new SetUpTrap. */
  public TrapBelt(Intake_Belt intakeBelt, Limit_Switch limitSwitch) {
    _intakeBelt = intakeBelt;
    _limitSwitch = limitSwitch;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(_intakeBelt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    _intakeBelt.IntakeBeltOut();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    _intakeBelt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return _limitSwitch.isPressed();
  }
}
