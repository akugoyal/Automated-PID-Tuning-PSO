package frc.robot.commands.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;
import frc.robot.util.Telemetry;
import frc.robot.PSO.Function;

public class PivotToAngleTimed extends Command {
    private RobotMap.Arm.Goal setpoint;

    private double counter;
    private double ref;
    private boolean first;
    private double lastTime;

    public PivotToAngleTimed(RobotMap.Arm.Goal goal) {
        counter = 0;
        setpoint = goal;
        first = true;
        addRequirements(Arm.getInstance());
    }

    public void execute() {
        if (first) {
            lastTime = System.currentTimeMillis();
            first = false;
        } else {
            counter += (System.currentTimeMillis() - lastTime);
            lastTime = System.currentTimeMillis();
        }

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

        Telemetry.putNumber("pivot", "counter", counter);
        Function.encoderDump.add(Arm.getInstance().getPosition());
        Arm.getInstance().moveToPosition(ref);
    }

    public boolean isFinished() {
        return (counter > RobotMap.PSO.TEST_LENGTH);
    }

    public void end(boolean interrupted) {
        Arm.getInstance().setPercentOutput(0);
    }

    public String getName() {
        switch (setpoint) {
            case ZERO:
                return "Zero";
            case SETPOINT1:
                return "30";
            case SETPOINT2:
                return "60";
            case SETPOINT3:
                return "90";
            default:
                return "Error";
        }
    }
}
