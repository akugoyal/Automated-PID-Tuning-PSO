package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
// import frc.robot.commands.pivot.PivotToAngle;
// import frc.robot.commands.pivot.PivotToAngleTimed;
import frc.robot.commands.pivot.ZeroPivot;

public class CommandGroups {
        // public static final Command FULL_ZERO = new ZeroPivot().alongWith(new ZeroElevator(), new ZeroIntake());

        // public static final Command RUN_ZERO = new ZeroPivot().andThen(new PivotToAngleTimed(RobotMap.Arm.Goal.SETPOINT)); //TODO figure out if this is correct
    
        // public static final Command FULL_INTAKE = new MoveNoteToShooter().raceWith(new IndexToShooter().alongWith(new IntakeNote(), new ZeroPivot()));
    
        // public static final Command FULL_SHOOT_SPEAKER = new PivotToAngle(RobotMap.Arm.Goal.SPEAKER).alongWith(new RevShooter(RobotMap.Shooter.Goal.SPEAKER)).andThen(new ShootNote());
    
        // public static final Command FULL_SHOOT_AMP = new PivotToAngle(RobotMap.Arm.Goal.AMP).alongWith(new RevShooter(RobotMap.Shooter.Goal.AMP)).andThen(new ShootNote());
        
        // public static final Command FULL_SHOOT_NO_ALIGN = new RevShooter(RobotMap.Arm.Goal.AMP).andThen(new ShootNote());
        
//        public static final Command PRE_DRIVEFWD_CLIMB = new PivotToAngle(RobotMap.Pivot.Goal.TRAP1); // wait for drive forward
//        public static final Command PRE_DRIVEBKWD_CLIMB = new PivotToAngle(RobotMap.Pivot.Goal.TRAP2).andThen(new MoveToPosition(RobotMap.Elevator.STAGE_HEIGHT).alongWith(new ZeroPivot())); // wait for drive backwards
//        public static final Command POST_DRIVEBKWD_CLIMB = new ZeroElevator();


//         public static final Command FULL_SHOOT_TRAP = new MoveToPosition(RobotMap.Elevator.TRAP_HEIGHT).alongWith(new PivotToAngle(RobotMap.Pivot.Goal.TRAP_SCORE))
                                                                                                //        .andThen(new ShooterManual(RobotMap.Shooter.Goal.AMP))
                                                                                                //        .andThen(new ZeroPivot(), new ZeroElevator());
    
}