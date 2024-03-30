package frc.robot.subsystems;

import frc.robot.Constants;

public class RightClaw extends Claw {

    public RightClaw() {
        super(Constants.ClimberConstants.rightClawMotorPower, Constants.ClimberConstants.rightClawMotor, "Right Claw Encoder");
    }
}
