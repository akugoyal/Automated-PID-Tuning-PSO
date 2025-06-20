package frc.robot.PSO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
// import frc.robot.commands.pivot.PivotToAngle;
import frc.robot.commands.pivot.PivotToAngleTimed;
import frc.robot.commands.pivot.ZeroPivot;
import frc.robot.subsystems.Arm;
import frc.robot.util.Telemetry;
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
        // System.out.println("kP: " + kP + "\tkI: " + kI + "\tkD: " + kD);
        Telemetry.putNumber("pivot", "Pivot kP", kP);
        Telemetry.putNumber("pivot", "Pivot kI", kI);
        Telemetry.putNumber("pivot", "Pivot kD", kD);
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

        BufferedWriter writer;

        double sp1 = 30.0;
        double sp2 = 60.0;
        double sp3 = 90.0;

        double error = 0.0;
        double sum = 0.0;
        
        // ZeroPivot zeroCommand = new ZeroPivot();
        // CommandScheduler.getInstance().schedule(zeroCommand);
        
        // Arm.getInstance().getController().reset();
        // while(!zeroCommand.isFinished()) {

        // }


        //round 1

        // try {
        //     writer = new BufferedWriter(new FileWriter(RobotMap.Arm.currentSaveFile_LOG), true)); //TODO modify file path if necessary
        // } catch (IOException e1) {
        //     e1.printStackTrace();
        //     throw new RuntimeException();
        // }
        String output = "";

        output += kP + "\n" + kI + "\n" + kD + "\n";

        System.out.println("\n\n\n\n\nRound 1");
        Arm.getInstance().getController().reset();
        System.out.println("R1 controller reset");
        PivotToAngleTimed pivotCommand = new PivotToAngleTimed(sp1);
        ZeroPivot zeroCommand = new ZeroPivot();
        Command zeroThenPivot = zeroCommand.andThen(pivotCommand);
        RobotMap.Arm.scheduleCmd = true;
        RobotMap.Arm.cmdToSchedule = zeroThenPivot;
        Telemetry.putNumber("pivot", "Cmd", 30);
        System.out.println("R1 cmd scheduled");
        while(!pivotCommand.isFinished()) {
            // System.out.println("whiling away");
             try {
                 Thread.sleep(20);
             } catch (InterruptedException e) {
                 System.out.println("WEE WOO");
             }
         } //TODO not sure if this works
        // try {
        //     Thread.sleep((long)RobotMap.PSO.TEST_LENGTH * 1000 + 1000);
        // } catch (InterruptedException e) {
        //     System.out.println("R1 INTERRUPTED");
        // }
        System.out.println("R1 thread slept");
        for(int i = 0; i < encoderDump.size(); i++) {
            sum += Math.pow(Math.abs(sp1 - encoderDump.get(i)), 2) + Math.pow(Math.abs(sp1 - encoderDump.get(i)), 0.5) + Math.abs(sp1 - encoderDump.get(i)) * (i * 100 / encoderDump.size());
            output += encoderDump.get(i) + " ";
        }
        System.out.println("R1 sum calculated");
        error += sum / ((double) encoderDump.size());
        System.out.println("R1 error added");


        output += "\n";

        // round 2
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\nRound 2");
        Arm.getInstance().getController().reset();
        encoderDump.clear();
        sum = 0.0;
        pivotCommand = new PivotToAngleTimed(sp2);
        zeroCommand = new ZeroPivot();
        zeroThenPivot = zeroCommand.andThen(pivotCommand);
        RobotMap.Arm.scheduleCmd = true;
        RobotMap.Arm.cmdToSchedule = zeroThenPivot;
        Telemetry.putNumber("pivot", "Cmd", 60);
        System.out.println("R2 cmd scheduled");
         while(!pivotCommand.isFinished()) {
             try {
                 Thread.sleep(20);
             } catch (InterruptedException e) {
                 System.out.println("WEE WOO");
             }
        } //TODO not sure if this works
        for(int i = 0; i < encoderDump.size(); i++) {
            sum += Math.pow(Math.abs(sp2 - encoderDump.get(i)), 2) + Math.pow(Math.abs(sp2 - encoderDump.get(i)), 0.5) + Math.abs(sp1 - encoderDump.get(i)) * (i * 100 / encoderDump.size());
            output += encoderDump.get(i) + " ";
        }
        System.out.println("R2 sum summed");
        error += sum / ((double) encoderDump.size());
        System.out.println("R2 error calculated");


        output += "\n";

        // round 3
        System.out.println("Round 3");
        Arm.getInstance().getController().reset();
        System.out.println("R3 controller reset");
        encoderDump.clear();
        System.out.println("R3 encoderdump cleared");
        sum = 0.0;
        pivotCommand = new PivotToAngleTimed(sp3);
        zeroCommand = new ZeroPivot();
        zeroThenPivot = zeroCommand.andThen(pivotCommand);
        RobotMap.Arm.scheduleCmd = true;
        RobotMap.Arm.cmdToSchedule = zeroThenPivot;
        Telemetry.putNumber("pivot", "Cmd", 90);
        System.out.println("R2 cmd scheduled");
         while(!pivotCommand.isFinished()) {
             try {
                 Thread.sleep(20);
             } catch (InterruptedException e) {
                 System.out.println("WEE WOO");
             }
         } //TODO not sure if this works


        System.out.println("R3 Thread slept");
        for(int i = 0; i < encoderDump.size(); i++) {
            sum += Math.pow(Math.abs(sp3 - encoderDump.get(i)), 2) + Math.pow(Math.abs(sp2 - encoderDump.get(i)), 0.5) + Math.abs(sp1 - encoderDump.get(i)) * (i * 100 / encoderDump.size());
            output += encoderDump.get(i) + " ";
        }
        System.out.println("R3 sum summed");
        error += sum / ((double) encoderDump.size());
        System.out.println("R3 error added");
        Telemetry.putNumber("pivot", "Particle error", error);
        Telemetry.putNumber("pivot", "Cmd", 30);

        output += "\n\n\n";

        // try {
        //     writer.write(output);
        //     writer.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     throw new RuntimeException();
        // }


        return error;
    }
    

}