// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.CommandGroups;
import frc.robot.subsystems.Arm;
import frc.robot.util.Telemetry;
import frc.robot.PSO.*;

/*
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  private SendableChooser<String> autonChooser;
  private Telemetry telemetry;

  private boolean firstPeriodic = false;

  private CANSparkMax driveRightFwd = new CANSparkMax(5, CANSparkLowLevel.MotorType.kBrushed); 
  private CANSparkMax driveRightBack = new CANSparkMax(8, CANSparkLowLevel.MotorType.kBrushed); 
  private CANSparkMax driveLeftFwd = new CANSparkMax(6, CANSparkLowLevel.MotorType.kBrushed); 
  private CANSparkMax driveLeftBack = new CANSparkMax(7, CANSparkLowLevel.MotorType.kBrushed); 
  BufferedWriter writer;
  frc.robot.PSO.Main pso;

  @Override
  public void robotInit() {
    // LiveWindow.setEnabled(true);
    // LiveWindow.enableAllTelemetry();

    // NetworkTableInstance.getDefault().getTable("SmartDashboard").delete;
    // smartDashboard.delete;

    // DataLogManager.start();
    // DriverStation.startDataLog(DataLogManager.getLog());

    // Limelight.setCameraPose(RobotMap.Camera.FORWARD, RobotMap.Camera.UP, RobotMap.Camera.PITCH);
    
    // CommandScheduler.getInstance().setDefaultCommand(Drivetrain.getInstance(), new SwerveManual());

    // NetworkTableInstance instance = NetworkTableInstance.getDefault();
    // if (RobotBase.isSimulation()) {
    //   instance.stopServer();
    //   instance.startClient4("localhost");
    // }
    // instance.startServer("telemetry.json", "127.0.10.72", 1072, 1072);
    // NetworkTable table = instance.getTable("test");
    // NetworkTableEntry test = table.getEntry("test");
    // test.setDouble(1072);

    telemetry = new Telemetry();

    driveRightFwd.setIdleMode(IdleMode.kBrake);
    driveRightBack.setIdleMode(IdleMode.kBrake);
    driveLeftFwd.setIdleMode(IdleMode.kBrake);
    driveLeftBack.setIdleMode(IdleMode.kBrake);

    // try {
    //   BufferedWriter out = new BufferedWriter(new FileWriter("/home/lvuser/savefile.txt"));
    //   out.write("hi" + 1);
    //   out.close();
    //   Runtime r = Runtime.getRuntime();
    //   Process p = r.exec("mv /home/lvuser/savefile.txt /U/savefile.txt");
    //   try {p.waitFor();} catch (InterruptedException e) {}
    //   System.out.println(Arrays.toString(File.listRoots()));
    //   System.out.println(Arrays.toString(new File("/").listFiles()));
    // } catch (IOException e) {
    //   e.printStackTrace();
    //   throw new RuntimeException("Failed to copy savefile to USB");
    // }

    // CommandScheduler.getInstance().setDefaultCommand(Arm.getInstance(), )
    // telemetry.startServer();
    // telemetry.swerveStates();

    // autonChooser = new SendableChooser<String>();
    // autonChooser.setDefaultOption("Four Note Path", "Four Note Path");
    // autonChooser.addOption("Six Note Path", "Six Note Path");
    // autonChooser.addOption("Three Note Path", "Three Note Path");
    // SmartDashboard.putData("Auton Chooser", autonChooser);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    // RobotMap.Field.FIELD.setRobotPose(Drivetrain.getInstance().getPoseEstimatorPose2d());

    // telemetry.autons("Current Auton", autonChooser.getSelected());

    telemetry.publish();
    // driveLeftFwd.setIdleMode(IdleMode.kBrake);
    // driveLeftBack.setIdleMode(IdleMode.kBrake);
    // driveRightFwd.setIdleMode(IdleMode.kBrake);
    // driveRightBack.setIdleMode(IdleMode.kBrake);

    // NetworkTableInstance.().flushLocal();
    // NetworkTableInstance.getDefault().flush();
  }

  @Override
  public void autonomousInit() {
    // switch (autonChooser.getSelected()) {
    //   case "Four Note Path":
    //     Drivetrain.getInstance().setPose(new Pose2d(1.28, 5.41, Rotation2d.fromDegrees(180)));
    //     Autons.fourNotePath.schedule();
    //     break;
    //   case "Three Note Path":
    //     Drivetrain.getInstance().setPose(new Pose2d(1.51, 1.36, Rotation2d.fromDegrees(180)));
    //     Autons.threeNotePath.schedule();
    //     break;
    //   case "Six Note Path":
    //     Drivetrain.getInstance().setPose(new Pose2d(1.72, 5.56, Rotation2d.fromDegrees(180)));
    //     Autons.sixNotePath.schedule();
    //   // default:
    //   //   Drivetrain.getInstance().setPose(Flip.apply(new Pose2d(1.28, 5.41, Rotation2d.fromDegrees(180))));
    //   //   Autons.fourNotePath.schedule();
    //   //   break;
    // }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

    CommandScheduler.getInstance().cancelAll();

    pso = new frc.robot.PSO.Main();
    Thread thread = new Thread(pso);

    // parseFileList();

    // DumpData dump = new DumpData();
    // Thread thread = new Thread(dump);
    thread.start();

    // try {
    //   writer = new BufferedWriter(new FileWriter("U/velsNew.txt", true)); //TODO modify file path if necessary
    // } catch (IOException e1) {
    //   e1.printStackTrace();
    //   throw new RuntimeException();
    // }

    thread.start();
  }

  public void parseFileList() {
    // Scanner in;
    // int numFiles;
    // try {
    //     in = new Scanner(new File("/U/FileList.txt")); //TODO
    //     numFiles = in.nextInt();
    // } catch (FileNotFoundException e) {
    //     e.printStackTrace();
    //     throw new RuntimeException();
    // }

    RobotMap.Arm.loadFiles_Log = new String[1];
    RobotMap.Arm.saveFiles_Log = new String[1];

    RobotMap.Arm.loadFiles_Log[0] = "/U/Bs/savefileB0.txt";
    RobotMap.Arm.saveFiles_Log[0] = "/U/Bs/savefileB0_overrun_LOG.txt";

    // for(int i = 0; i < numFiles; i++) {
    //   RobotMap.Arm.loadFiles[i] = new String[2];
    //   RobotMap.Arm.loadFiles[i][0] = ("U" + in.next().split(":")[1]).replaceAll("\\\\", "/");
    //   // System.out.println(RobotMap.Arm.loadFiles[i][0]);
    //   // System.out.println(RobotMap.Arm.loadFiles[i][0].indexOf("."));
    //   // System.out.println(Arrays.toString(RobotMap.Arm.loadFiles[i][0].split("[.]")));
    //   RobotMap.Arm.loadFiles[i][1] = RobotMap.Arm.loadFiles[i][0].split("[.]")[0]+"_LOG.txt";
    // }
  }

  public static double initTime;
  public static boolean go = false;
  @Override
  public void teleopPeriodic() {
    if (firstPeriodic) {
      CommandScheduler.getInstance().cancelAll();
      initTime = System.currentTimeMillis();
      firstPeriodic = false;
      System.out.println("Canceled");
    }
    
    if (go) {
      initTime = System.currentTimeMillis();
      go = false;
    }
    // try {
    //   writer.write("" + (System.currentTimeMillis() - initTime) + ", " + Double.toString(Arm.getInstance().getPosition())+"\n");
    // } catch (IOException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }
    if(RobotMap.Arm.scheduleCmd) {
      RobotMap.Arm.scheduleCmd = false;
      CommandScheduler.getInstance().schedule(RobotMap.Arm.cmdToSchedule);
    }
    
    // Intake.getInstance().setRollerPower(RobotMap.Intake.ROLLER_SPEED);
    // Shooter.getInstance().setShooter(RobotMap.Shooter.SHOOTING_SPEED);
    Telemetry.putBoolean("pivot", "Is Stalling", Arm.getInstance().isStalling());
    if (Arm.getInstance().isStalling()) {
      System.out.println("pivot stallinggggg");
    }
  }

  @Override
  public void disabledInit() {
    try {
      if (writer != null) {
        writer.close();
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // telemetry.closeServer();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {
    
  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}