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

public class Intake_Bar extends SubsystemBase {

  //private CANSparkMax intakeMotor;
  private final CANSparkMax bottomIntakeMotor = new CANSparkMax(Constants.IntakeConstants.intakeBarMotor, MotorType.kBrushless);
  private final RelativeEncoder encoder;
  private double bottomIntakeMotorPower = Constants.IntakeConstants.intakeBarMotorPower;
  // private final String sEncoderName;

  public Intake_Bar() {
    setDefaultCommand(new RunCommand(this::stop, this));

    //set up right claw motor settings
    bottomIntakeMotor.restoreFactoryDefaults();
    bottomIntakeMotor.setIdleMode(IdleMode.kBrake);
    //clawMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    //clawMotor.setSoftLimit(SoftLimitDirection.kReverse, ClimberConstants.rightClawLimitIn);
    //clawMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    //clawMotor.setSoftLimit(SoftLimitDirection.kForward, ClimberConstants.rightClawLimitOut);
    bottomIntakeMotor.burnFlash();
    encoder = bottomIntakeMotor.getEncoder();
  }
  public void stop() {
    bottomIntakeMotor.stopMotor();
  }

  public void IntakeBarIn() {
    bottomIntakeMotor.set(bottomIntakeMotorPower);
  }

  public void IntakeBarOut() {
    bottomIntakeMotor.set(-bottomIntakeMotorPower); 
  }

  public void SetBarIntakePower(double intakePower) {
    bottomIntakeMotor.set(intakePower);
  }

  public double getPosition () {
    return encoder.getPosition();
  }

  public void TrapBar(){
    if (frc.robot.subsystems.Limit_Switch.limitSwitch.get() == true){
      bottomIntakeMotor.set(-bottomIntakeMotorPower);
    }
    else {
      bottomIntakeMotor.stopMotor();
    }
  }

  @Override
  public void periodic() {
    //figure out how to get the shuffleboard to output the position of the encoder here
    //SmartDashboard.putNumber("Bar Intake", encoder.getPosition());
  }
}

