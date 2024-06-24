package frc.robot.commands.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;
import frc.robot.util.MathUtil;

// public class PivotToAngle extends Command {
//     private RobotMap.Arm.Goal setpoint;
//     private double ref;

//     public PivotToAngle(RobotMap.Arm.Goal goal) {
//         setpoint = goal;
//         addRequirements(Arm.getInstance());
//     }

//     public void execute() {
//         switch (setpoint) {
//             case ZERO:
//                 ref = 0;
//                 break;
//             case SETPOINT1:
//                 ref = 30.0;
//                 break;
//             case SETPOINT2:
//                 ref = 60.0;
//                 break;
//             case SETPOINT3:
//                 ref = 90.0;
//                 break;
//         }
//         Arm.getInstance().moveToPosition(ref);

//     }

//     public boolean isFinished() {
//         return MathUtil.compareSetpoint(Arm.getInstance().getPosition(), ref, RobotMap.Arm.MAX_ERROR);
//     }

//     // public void end(boolean interrupted) {
//     //     Pivot.getInstance().setPercentOutput(0);
//     // }

// }
