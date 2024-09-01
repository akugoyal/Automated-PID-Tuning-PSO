package frc.robot.commands.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.*;
import frc.robot.subsystems.Arm;

public class ZeroPivot extends Command {
    double initTime;
    public ZeroPivot() {
        initTime = System.currentTimeMillis();
        addRequirements(Arm.getInstance());
    }

    public void execute() {
        Arm.getInstance().setPercentOutput(RobotMap.Arm.ZERO_SPEED, true);
    }

    public boolean isFinished() {
        return Arm.getInstance().isStalling() && System.currentTimeMillis() - initTime > 500;
    }

    public void end(boolean interrupted) {
        Arm.getInstance().setPercentOutput(0, false);
        Arm.getInstance().setSensorPosition(0);
        Robot.go = true;
    }
    
}