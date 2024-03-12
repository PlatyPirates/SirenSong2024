// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
//importing extra wpilib classes
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class Trap_Rollers extends SubsystemBase {

  //private CANSparkMax intakeMotor;
  private final CANSparkMax topIntakeMotor = new CANSparkMax(Constants.IntakeConstants.trapRollerMotor, MotorType.kBrushless);
  private final RelativeEncoder encoder;
  private double topIntakeMotorPower = Constants.IntakeConstants.trapRollerMotorPower;
  // private final String sEncoderName;

  public Trap_Rollers() {
    setDefaultCommand(new RunCommand(this::stop, this));

    //set up right claw motor settings
    topIntakeMotor.restoreFactoryDefaults();
    topIntakeMotor.setIdleMode(IdleMode.kBrake);
    //clawMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    //clawMotor.setSoftLimit(SoftLimitDirection.kReverse, ClimberConstants.rightClawLimitIn);
    //clawMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    //clawMotor.setSoftLimit(SoftLimitDirection.kForward, ClimberConstants.rightClawLimitOut);
    topIntakeMotor.burnFlash();
    encoder = topIntakeMotor.getEncoder();
  }
  public void stop() {
    topIntakeMotor.stopMotor();
  }

  public void TrapRollersIn() {
    topIntakeMotor.set(topIntakeMotorPower);
  }

  public void TrapRollersOut() {
    topIntakeMotor.set(-topIntakeMotorPower); 
  }

  public void SetTrapRollerPower(double intakePower) {
    topIntakeMotor.set(intakePower);
  }

  public double getPosition () {
    return encoder.getPosition();
  }

  @Override
  public void periodic() {
    //figure out how to get the shuffleboard to output the position of the encoder here
    SmartDashboard.putNumber("Trap Rollers", encoder.getPosition());
  }
}

