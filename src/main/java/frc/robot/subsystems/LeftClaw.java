package frc.robot.subsystems;

import frc.robot.Constants;

public class LeftClaw extends Claw {
    
    public LeftClaw() {
        super(Constants.ClimberConstants.leftClawMotorPower, Constants.ClimberConstants.leftClawMotor, "Left Claw Encoder");
    }
}
