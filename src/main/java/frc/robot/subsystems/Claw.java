

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
import frc.robot.Constants.ClimberConstants;

public abstract class Claw extends SubsystemBase {

  private CANSparkMax clawMotor;
  private final RelativeEncoder encoder;
  private double dClawMotorPower = 0;
  private final String sEncoderName;

  public Claw(double dMotorPower, int iMotor, String encoderName) {
    dClawMotorPower = dMotorPower;
    clawMotor = new CANSparkMax(iMotor, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
    encoder = clawMotor.getEncoder();
    sEncoderName = encoderName;
    setDefaultCommand(new RunCommand(this::stop, this));

    //set up right claw motor settings
    clawMotor.restoreFactoryDefaults();
    clawMotor.setIdleMode(IdleMode.kBrake);
    clawMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    clawMotor.setSoftLimit(SoftLimitDirection.kReverse, ClimberConstants.rightClawLimitIn);
    clawMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    clawMotor.setSoftLimit(SoftLimitDirection.kForward, ClimberConstants.rightClawLimitOut);
    clawMotor.burnFlash();
  }
  public void stop() {
    clawMotor.stopMotor();
  }

  public void ClawUp() {
    clawMotor.set(dClawMotorPower);
  }

  public void ClawDown() {
    clawMotor.set(-dClawMotorPower); 
  }

  public void SetClawPower(double clawPower) {
    clawMotor.set(clawPower);
  }

  public double getPosition () {
    return encoder.getPosition();
  }

  @Override
  public void periodic() {
    //figure out how to get the shuffleboard to output the position of the encoder here
    SmartDashboard.putNumber(sEncoderName, encoder.getPosition());
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
