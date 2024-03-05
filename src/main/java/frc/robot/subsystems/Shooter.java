// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  private final CANSparkMax _shooterMotor = new CANSparkMax(Constants.ShooterConstants.shooterMotor, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);

  private RelativeEncoder _shooterEncoder;

  public Shooter() {
    _shooterMotor.restoreFactoryDefaults();
    enableOpenLoopRampRate(true);
    _shooterEncoder = _shooterMotor.getEncoder();
    encoderReset();
    _shooterMotor.burnFlash();
  }

  public void enableOpenLoopRampRate(boolean enable) {
    double rampRate = (enable ? Constants.DrivetrainConstants.rampRate : 0.0);

    _shooterMotor.setOpenLoopRampRate(rampRate);
  }

  public void setShooter(double power) {
    _shooterMotor.set(power);
  }

  @Override
  public void periodic() {
  }

  public void encoderReset() {
    _shooterEncoder.setPosition(0.0);
  }

  public double getPosition() {
    return _shooterEncoder.getPosition();
  }
}
