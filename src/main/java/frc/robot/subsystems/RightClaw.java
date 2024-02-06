

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
//importing extra wpilib classes
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

import frc.robot.Constants.IntakeConstants;

public class RightClaw extends SubsystemBase {

  private CANSparkMax _rightClawMotor = new CANSparkMax(Constants.ClimberConstants.rightClawMotor, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
  private final RelativeEncoder _encoder = _rightClawMotor.getEncoder();
  private double _rightClawMotorPower = Constants.ClimberConstants.rightClawMotorPower;

  public RightClaw() {
    setDefaultCommand(new RunCommand(this::stop, this));

    //set up right claw motor settings
    _rightClawMotor.restoreFactoryDefaults();
    _rightClawMotor.setIdleMode(IdleMode.kBrake);
    _rightClawMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    _rightClawMotor.setSoftLimit(SoftLimitDirection.kReverse, IntakeConstants.armLimitIn);
    _rightClawMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    _rightClawMotor.setSoftLimit(SoftLimitDirection.kForward, IntakeConstants.armLimitOut);
    _rightClawMotor.burnFlash();
  }
  public void stop() {
    _rightClawMotor.stopMotor();
  }

  public void rightClawUp() {
    _rightClawMotor.set(_rightClawMotorPower);
  }

  public void rightClawDown() {
    _rightClawMotor.set(-_rightClawMotorPower); 
  }

  public void go(double clawPower) {
    _rightClawMotor.set(clawPower);
  }

  public void setClawPower (double clawPower) {
    _rightClawMotorPower = clawPower; 
  }

  public double getPosition () {
    return _encoder.getPosition();
  }

  @Override
  public void periodic() {
    //figure out how to get the shuffleboard to output the position of the encoder here
    SmartDashboard.putNumber("Right motor encoder value: ", _encoder.getPosition());
  }
  
  /**
   * Example command factory method.
   *
   * @return a command
   
  public void setClimber(double speed) {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here 

        });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
  */
}
