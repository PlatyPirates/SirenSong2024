// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Flap extends SubsystemBase {

  private final PWMSparkMax _flapMotor = new PWMSparkMax(Constants.ShooterConstants.flapMotor);

  public Flap() {
  }


  public void setFlap(double power) {
    _flapMotor.set(power);
  }

  public void fullForward() {
    setFlap(2.003);
  }

  public void fullReverse() {
    setFlap(0.999);
  }

  @Override
  public void periodic() {
  }
}
