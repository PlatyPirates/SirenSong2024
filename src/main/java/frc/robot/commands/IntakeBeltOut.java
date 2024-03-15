// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.Intake_Belt;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeBeltOut extends Command {
  /** Creates a new IntakeIn. */ 
  private final Intake_Belt _outtake; 

  public IntakeBeltOut(Intake_Belt outtake) {
    _outtake = outtake;
    // Use addRequirements() here to declare subsystem dependencies
    addRequirements(_outtake);
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
    return false;
  }
}
