package frc.robot.commands.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;

public class ZeroPivot extends Command {
    public ZeroPivot() {
        addRequirements(Arm.getInstance());
    }

    public void execute() {
        Arm.getInstance().setPercentOutput(RobotMap.Arm.ZERO_SPEED);
    }

    public boolean isFinished() {
        return Arm.getInstance().isStalling();
    }

    public void end(boolean interrupted) {
        Arm.getInstance().setPercentOutput(0);
        Arm.getInstance().setSensorPosition(0);
    }
    
}