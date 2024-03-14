// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Limit_Switch;
import frc.robot.subsystems.Trap_Rollers;

public class DetectSwitch extends Command {
  private final Limit_Switch limitSwitch;
  private final Trap_Rollers trapRollers;
  /** Creates a new DetectSwitch. */
  public DetectSwitch(Trap_Rollers _trapRollers, Limit_Switch _limitSwitch) {
    // Use addRequirements() here to declare subsystem dependencies.
    limitSwitch = _limitSwitch;
    trapRollers = _trapRollers;
    addRequirements(trapRollers);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return limitSwitch.isPressed();
  }
}
