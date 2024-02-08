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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.ClimberConstants;

public class LeftClaw extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private CANSparkMax _leftClawMotor = new CANSparkMax(Constants.ClimberConstants.leftClawMotor, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
  private final RelativeEncoder _encoder = _leftClawMotor.getEncoder();
  private double _leftClawMotorPower = Constants.ClimberConstants.leftClawMotorPower;
  public LeftClaw() {
    setDefaultCommand(new RunCommand(this::stop, this));
    
    //set up left claw motor settings
    _leftClawMotor.restoreFactoryDefaults();
    _leftClawMotor.setIdleMode(IdleMode.kBrake);
    _leftClawMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    _leftClawMotor.setSoftLimit(SoftLimitDirection.kReverse, ClimberConstants.leftClawLimitIn);
    _leftClawMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    _leftClawMotor.setSoftLimit(SoftLimitDirection.kForward, ClimberConstants.leftClawLimitOut);
    _leftClawMotor.burnFlash();

  }
  public void stop() {
    _leftClawMotor.stopMotor();
  }

  public void leftClawUp() {
    _leftClawMotor.set(_leftClawMotorPower);
  }

  public void leftClawDown() {
    _leftClawMotor.set(-_leftClawMotorPower); 
  }

  public void go(double clawPower) {
    _leftClawMotor.set(clawPower);
  }

  public void setArmPower (double clawPower) {
    _leftClawMotorPower = clawPower; 
  }

  public double getPosition () {
    return _encoder.getPosition();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Left motor encoder value: ", _encoder.getPosition());
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
