// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.RightClaw;

public class RightClawDown extends Command {
  private final RightClaw _rightClaw;

  /** Creates a new PivotUp. */
  public RightClawDown(RightClaw rightClaw) {
    _rightClaw = rightClaw;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(_rightClaw);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    _rightClaw.ClawDown();
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
