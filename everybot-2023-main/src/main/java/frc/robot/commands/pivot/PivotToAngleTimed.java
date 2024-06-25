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

    public PivotToAngleTimed(RobotMap.Arm.Goal goal) {
        counter = 0;
        setpoint = goal;
        addRequirements(Arm.getInstance());
    }

    public void execute() {
      counter += RobotMap.ROBOT_LOOP;

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

        // Function.encoderDump.add(Arm.getInstance().getPosition());
        Arm.getInstance().moveToPosition(ref);
    }

    public boolean isFinished() {
        return (counter > RobotMap.PSO.TEST_LENGTH);
    }

    public void end(boolean interrupted) {
        counter = 0;
        Arm.getInstance().setPercentOutput(0);
    }

}
