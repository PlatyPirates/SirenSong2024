

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
//importing extra wpilib classes
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public abstract class Intake_Belt extends SubsystemBase {

  private final CANSparkMax beltIntakeMotor = new CANSparkMax(Constants.IntakeConstants.intakeBeltMotor, MotorType.kBrushless);
  private final RelativeEncoder encoder;
  private double intakeMotorPower = Constants.IntakeConstants.intakeBeltMotorPower;

  public Intake_Belt() {
    setDefaultCommand(new RunCommand(this::stop, this));

    //set up right claw motor settings
    beltIntakeMotor.restoreFactoryDefaults();
    beltIntakeMotor.setIdleMode(IdleMode.kBrake);
    //clawMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    //clawMotor.setSoftLimit(SoftLimitDirection.kReverse, ClimberConstants.rightClawLimitIn);
    //clawMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    //clawMotor.setSoftLimit(SoftLimitDirection.kForward, ClimberConstants.rightClawLimitOut);
    beltIntakeMotor.burnFlash();
    encoder = beltIntakeMotor.getEncoder();
  }
  public void stop() {
    beltIntakeMotor.stopMotor();
  }

  public void IntakeBeltIn() {
    beltIntakeMotor.set(intakeMotorPower);
  }

  public void IntakeBeltOut() {
    beltIntakeMotor.set(-intakeMotorPower); 
  }

  public void SetIntakePower(double Power) {
    beltIntakeMotor.set(Power);
  }

  public double getPosition () {
    return encoder.getPosition();
  }

  public void TrapBelt(){
    if (frc.robot.subsystems.Limit_Switch.limitSwitch.get() == true){
      beltIntakeMotor.set(-intakeMotorPower);
    }
    else {
      beltIntakeMotor.stopMotor();
    }
  }

  @Override
  public void periodic() {
    //figure out how to get the shuffleboard to output the position of the encoder here
    //SmartDashboard.putNumber("Intake Motor", encoder.getPosition());
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
