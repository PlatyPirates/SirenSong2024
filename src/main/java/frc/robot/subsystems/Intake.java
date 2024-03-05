// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  private final CANSparkMax _intakeMotor = new CANSparkMax(Constants.ShooterConstants.intakeMotor, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);

  private RelativeEncoder _intakeEncoder;

  public Intake() {
    _intakeMotor.restoreFactoryDefaults();
    enableOpenLoopRampRate(true);
    _intakeEncoder = _intakeMotor.getEncoder();
    encoderReset();
    _intakeMotor.burnFlash();
  }

  public void enableOpenLoopRampRate(boolean enable) {
    double rampRate = (enable ? Constants.DrivetrainConstants.rampRate : 0.0);

    _intakeMotor.setOpenLoopRampRate(rampRate);
  }

  public void setIntake(double power) {
    _intakeMotor.set(power);
  }

  @Override
  public void periodic() {
  }

  public void encoderReset() {
    _intakeEncoder.setPosition(0.0);
  }

  public double getPosition() {
    return _intakeEncoder.getPosition();
  }
}
