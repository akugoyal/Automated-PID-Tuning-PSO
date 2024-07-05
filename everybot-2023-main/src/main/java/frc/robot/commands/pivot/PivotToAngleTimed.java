package frc.robot.commands.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;
import frc.robot.util.Telemetry;
import frc.robot.PSO.Function;

public class PivotToAngleTimed extends Command {
    private double counter;
    private double ref;
    private boolean first;
    private double lastTime;

    public PivotToAngleTimed(double goal) {
        Arm.getInstance().getController().reset();
        Arm.getInstance().getController().setP(RobotMap.Arm.PIVOT_kP);
        Arm.getInstance().getController().setI(RobotMap.Arm.PIVOT_kI);
        Arm.getInstance().getController().setD(RobotMap.Arm.PIVOT_kD);
        counter = 0;
        ref = goal;
        first = true;
        addRequirements(Arm.getInstance());
    }

    public void execute() {
        if (first) {
            counter = 0;
            lastTime = System.currentTimeMillis();
            first = false;
        } else {
            counter += (System.currentTimeMillis() - lastTime) / 1000.0;
            lastTime = System.currentTimeMillis();
        }

        Telemetry.putNumber("pivot", "counter", counter);
        Function.encoderDump.add(Arm.getInstance().getPosition());
        Arm.getInstance().moveToPosition(ref);
    }

    public boolean isFinished() {
        return (counter > RobotMap.PSO.TEST_LENGTH);
    }

    public void end(boolean interrupted) {
        Arm.getInstance().setPercentOutput(0, false);
        Arm.getInstance().getController().reset();
        Arm.getInstance().getController().setP(RobotMap.Arm.PIVOT_kP);
        Arm.getInstance().getController().setI(RobotMap.Arm.PIVOT_kI);
        Arm.getInstance().getController().setD(RobotMap.Arm.PIVOT_kD);
        // counter = 0;
        first = true;
    }

    public String getName() {
        return Double.toString(ref);
    }
}
