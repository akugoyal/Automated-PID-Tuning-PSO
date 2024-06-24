package frc.robot.commands.pivot;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;
import frc.robot.util.MathUtil;
import frc.robot.PSO.Function;

public class PivotToAngleTimed extends Command {
    private RobotMap.Arm.Goal setpoint;
    private double ref;

    public PivotToAngleTimed(RobotMap.Arm.Goal goal) {
        setpoint = goal;
        addRequirements(Arm.getInstance());
    }

    public void execute() {
        switch (setpoint) {
            case ZERO:
                ref = 0;
                break;
            case SETPOINT1:
                ref = 30.0;
                break;
            case SETPOINT2:
                ref = 60.0;
                break;
            case SETPOINT3:
                ref = 90.0;
                break;
        }

        Timer timer = new Timer();

        Arm.getInstance().moveToPosition(ref);
        timer.schedule(new TimerTask() {
           @Override
           public void run() {
              Function.encoderDump.add(Arm.getInstance().getPosition());
           }
        }, 2500);

    }

    public boolean isFinished() {
        return MathUtil.compareSetpoint(Arm.getInstance().getPosition(), ref, RobotMap.Arm.MAX_ERROR);
    }

    // public void end(boolean interrupted) {
    //     Pivot.getInstance().setPercentOutput(0);
    // }

}
