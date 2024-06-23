// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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

/**
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
    // Autons.fourNotePath.cancel();
    // Autons.threeNotePath.cancel();
    // Autons.sixNotePath.cancel();
    // Drivetrain.getInstance().setYaw(0);

    pso = new frc.robot.PSO.Main();
  }

  @Override
  public void teleopPeriodic() {
    // Intake.getInstance().setRollerPower(RobotMap.Intake.ROLLER_SPEED);
    // Shooter.getInstance().setShooter(RobotMap.Shooter.SHOOTING_SPEED);
    Telemetry.putBoolean("pivot", "Is Stalling", Arm.getInstance().isStalling());
    if (Arm.getInstance().isStalling()) {
      System.out.println("pivot stallinggggg");
    }
  }

  @Override
  public void disabledInit() {
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