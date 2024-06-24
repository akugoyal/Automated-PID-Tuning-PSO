package frc.robot.PSO;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
// import frc.robot.commands.pivot.PivotToAngle;
import frc.robot.commands.pivot.PivotToAngleTimed;
import frc.robot.commands.pivot.ZeroPivot;
import frc.robot.subsystems.Arm;
import frc.robot.RobotMap;

public class Function {

    public static ArrayList<Double> encoderDump;

    /**
     * Calculate the result of (x^4)-2(x^3).
     * Domain is (-infinity, infinity).
     * Minimum is -1.6875 at x = 1.5.
     * @param x     the x component
     * @return      the y component
     */
    static double functionA (double x) {
        return Math.pow(x, 4) - 2 * Math.pow(x, 3);
    }

    /**
     * Perform Ackley's function.
     * Domain is [5, 5]
     * Minimum is 0 at x = 0 & y = 0.
     * @param x     the x component
     * @param y     the y component
     * @return      the z component
     */
    static double ackleysFunction (double x, double y) {
        double p1 = -20*Math.exp(-0.2*Math.sqrt(0.5*((x*x)+(y*y))));
        double p2 = Math.exp(0.5*(Math.cos(2*Math.PI*x)+Math.cos(2*Math.PI*y)));
        return p1 - p2 + Math.E + 20;
    }

    /**
     * Perform Booth's function.
     * Domain is [-10, 10]
     * Minimum is 0 at x = 1 & y = 3.
     * @param x     the x component
     * @param y     the y component
     * @return      the z component
     */
    static double boothsFunction (double x, double y) {
        double p1 = Math.pow(x + 2*y - 7, 2);
        double p2 = Math.pow(2*x + y - 5, 2);
        return p1 + p2;
    }

    /**
     * Perform the Three-Hump Camel function.
     * @param x     the x component
     * @param y     the y component
     * @return      the z component
     */
    static double threeHumpCamelFunction (double x, double y) {
        double p1 = 2*x*x;
        double p2 = 1.05*Math.pow(x, 4);
        double p3 = Math.pow(x, 6) / 6;
        return p1 - p2 + p3 + x*y + y*y;
    }

    static double errorFunction(double kP, double kI, double kD) {

        // RobotMap.Arm.PIVOT_kP = kP;
        // RobotMap.Arm.PIVOT_kI = kI;
        // RobotMap.Arm.PIVOT_kD = kD;

        encoderDump = new ArrayList<>();
        Arm.getInstance().getController().setP(kP);
        Arm.getInstance().getController().setI(kI);
        Arm.getInstance().getController().setD(kD);
        
    /**
     * Setpoints (can be modified to suit your needs)
     */
        double sp1 = 30.0;
        double sp2 = 60.0;
        double sp3 = 90.0;

        double error = 0.0;
        double sum = 0.0;
        
        ZeroPivot zeroCommand = new ZeroPivot();
        CommandScheduler.getInstance().schedule(zeroCommand);
        while(!zeroCommand.isFinished()) {

        }


        //round 1
        System.out.println("\n\n\n\n\nRound 1");
        PivotToAngleTimed pivotCommand = new PivotToAngleTimed(RobotMap.Arm.Goal.SETPOINT1);
        zeroCommand = new ZeroPivot();

        Command zeroThenPivot = pivotCommand;

        CommandScheduler.getInstance().schedule(zeroThenPivot);

        System.out.println("Starting pivot");
        while(!zeroThenPivot.isFinished()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }; //TODO not sure if this works

        for(double x : encoderDump) {
            sum += Math.abs(sp1 - x);
        }

        error += sum / encoderDump.size();
        
        Arm.getInstance().getController().reset();


        // round 2
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\nRound 2");
        encoderDump.clear();
        sum = 0.0;

        pivotCommand = new PivotToAngleTimed(RobotMap.Arm.Goal.SETPOINT2); //TODO not sure if this is necessary or i can just make a group
        zeroCommand = new ZeroPivot(); 

        zeroThenPivot = pivotCommand;

        CommandScheduler.getInstance().schedule(zeroThenPivot);

        while(!zeroThenPivot.isFinished()) {}; //TODO not sure if this works

        System.out.println("\n\n\n\n\n\n\n\nDONE MOVING");
        for(double x : encoderDump) {
            sum += Math.abs(sp2 - x);
        }

        error += sum / encoderDump.size();

        Arm.getInstance().getController().reset();

        // round 3
        System.out.println("Round 3");
        encoderDump.clear();
        sum = 0.0;

        pivotCommand = new PivotToAngleTimed(RobotMap.Arm.Goal.SETPOINT3); //TODO not sure if this is necessary or i can just make a group
        zeroCommand = new ZeroPivot(); 

        zeroThenPivot = pivotCommand;

        CommandScheduler.getInstance().schedule(zeroThenPivot);

        while(!zeroThenPivot.isFinished()) {}; //TODO not sure if this works

        for(double x : encoderDump) {
            sum += Math.abs(sp3 - x);
        }

        error += sum / encoderDump.size();


        return error;
    }
    

}