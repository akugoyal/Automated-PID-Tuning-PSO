// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.hardware.*;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot{
  /*
   * Autonomous selection options.
   */
  private static final String kNothingAuto = "do nothing";
  private static final String kConeAuto = "cone";
  private static final String kCubeAuto = "cube";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  Drivetrain dt = new Drivetrain();

  // CANCoder test = new CANCoder(0)

  //CANcoder leftCoder = new CANcoder(35, "rio");
  /*
   * Drive motor controller instances.
   * 
   * Change the id's to match your robot.
   * Change kBrushed to kBrushless if you are using NEO's.
   * Use the appropriate other class if you are using different controllers.
   */
  // CANSparkMax driveLeftSpark = new CANSparkMax(7, MotorType.kBrushed);
  // CANSparkMax driveRightSpark = new CANSparkMax(5, MotorType.kBrushed);
  // CANSparkMax driveLeftSparkTwo = new CANSparkMax(6, MotorType.kBrushed);
  // CANSparkMax driveRightSparkTwo = new CANSparkMax(8, MotorType.kBrushed);
  /*
   * Mechanism motor controller instances.
   * 
   * Like the drive motors, set the CAN id's to match your robot or use different
   * motor controller classses (TalonFX, TalonSRX, Spark, VictorSP) to match your
   * robot.
   * 
   * The arm is a NEO on Everybud.
   * The intake is a NEO 550 on Everybud.
   */
  CANSparkMax intake = new CANSparkMax(9, MotorType.kBrushless);
  TalonSRX arm = new TalonSRX(14);

  /**
   * The starter code uses the most generic joystick class.
   * 
   * The reveal video was filmed using a logitech gamepad set to
   * directinput mode (switch set to D on the bottom). You may want
   * to use the XBoxController class with the gamepad set to XInput
   * mode (switch set to X on the bottom) or a different controller
   * that you feel is more comfortable.
   */
  XboxController j = new XboxController(0);

  /*
   * Magic numbers. Use these to adjust settings.
   */

  /**
   * How many amps the arm motor can use.
   */
  static final int ARM_CURRENT_LIMIT_A = 20;

  /**
   * Percent output to run the arm up/down at
   */
  static final double ARM_OUTPUT_POWER = 1;

  /**
   * How many amps the intake can use while picking up
   */
  static final int INTAKE_CURRENT_LIMIT_A = 25;

  /**
   * How many amps the intake can use while holding
   */
  static final int INTAKE_HOLD_CURRENT_LIMIT_A = 5;

  /**
   * Percent output for intaking
   */
  static final double INTAKE_OUTPUT_POWER = 1.0;

  /**
   * Percent output for holding
   */
  static final double INTAKE_HOLD_POWER = 0.07;

  /**
   * Time to extend or retract arm in auto
   */
  static final double ARM_EXTEND_TIME_S = 2.0;

  /**
   * Time to throw game piece in auto
   */
  static final double AUTO_THROW_TIME_S = 0.375;

  /**
   * Time to drive back in auto
   */
  static final double AUTO_DRIVE_TIME = 6.0;

  /**
   * Speed to drive backwards in auto
   */
  static final double AUTO_DRIVE_SPEED = -0.25;

  // static Speed dtSpeed = new Speed();
  // static boolean changeSpeed = false;

  /**
   * This method is run once when the robot is first started up.
   */
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    m_chooser.setDefaultOption("do nothing", kNothingAuto);
    m_chooser.addOption("cone and mobility", kConeAuto);
    m_chooser.addOption("cube and mobility", kCubeAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    /*
     * You will need to change some of these from false to true.
     * 
     * In the setDriveMotors method, comment out all but 1 of the 4 calls
     * to the set() methods. Push the joystick forward. Reverse the motor
     * if it is going the wrong way. Repeat for the other 3 motors.
    //  */
    // driveLeftSpark.setInverted(false);
    // driveLeftSparkTwo.setInverted(false);
    // driveRightSpark.setInverted(false);
    // driveRightSparkTwo.setInverted(false);

    /*
     * Set the arm and intake to brake mode to help hold position.
     * If either one is reversed, change that here too. Arm out is defined
     * as positive, arm in is negative.
     */
    arm.setInverted(false);
    arm.setNeutralMode(NeutralMode.Brake);
    arm.configPeakCurrentLimit(ARM_CURRENT_LIMIT_A);
    intake.setInverted(false);
    intake.setIdleMode(IdleMode.kBrake);
  }

  /**
   * Calculate and set the power to apply to the left and right
   * drive motors.
   * 
   * @param turn Desired forward speed. Positive is forward.
   * @param forward    Desired turning speed. Positive is counter clockwise from
   *                above.
   */
//   public void setDriveMotors(double turn, double forward) {
//     if (turn == 0 && forward == 0) {
//       driveLeftSpark.setIdleMode(IdleMode.kBrake);
//       driveLeftSparkTwo.setIdleMode(IdleMode.kBrake);
//       driveRightSpark.setIdleMode(IdleMode.kBrake);
//       driveRightSparkTwo.setIdleMode(IdleMode.kBrake);
//     }
//     SmartDashboard.putNumber("drive forward power (%)", forward);
//     SmartDashboard.putNumber("drive turn power (%)", turn);

//     /*
//      * positive turn = counter clockwise, so the left side goes backwards
//      */
//     double left = turn - forward;
//     double right = turn + forward;

//     SmartDashboard.putNumber("drive left power (%)", left);
//     SmartDashboard.putNumber("drive right power (%)", -right);

//     // see note above in robotInit about commenting these out one by one to set
//     // directions.
//     driveLeftSpark.set(left);
//     driveLeftSparkTwo.set(left);
//     driveRightSpark.set(right);
//     driveRightSparkTwo.set(right);
// }

  /**
   * Set the arm output power. Positive is out, negative is in.
   * 
   * @param percent
   */
  public void setArmMotor(double percent) {
    arm.set(TalonSRXControlMode.PercentOutput, percent);;
    SmartDashboard.putNumber("arm power (%)", percent);
  }

  /**
   * Set the arm output power.
   * 
   * @param percent desired speed
   * @param amps current limit
   */
  public void setIntakeMotor(double percent, int amps) {
    intake.set(percent);
    intake.setSmartCurrentLimit(amps);
    SmartDashboard.putNumber("intake power (%)", percent);
    SmartDashboard.putNumber("intake motor current (amps)", intake.getOutputCurrent());
    SmartDashboard.putNumber("intake motor temperature (C)", intake.getMotorTemperature());
  }

  /**
   * This method is called every 20 ms, no matter the mode. It runs after
   * the autonomous and teleop specific period methods.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Time (seconds)", Timer.getFPGATimestamp());
  }

  double autonomousStartTime;
  double autonomousIntakePower;

  @Override
  public void autonomousInit() {
    // driveLeftSpark.setIdleMode(IdleMode.kBrake);
    // driveLeftSparkTwo.setIdleMode(IdleMode.kBrake);
    // driveRightSpark.setIdleMode(IdleMode.kBrake);
    // driveRightSparkTwo.setIdleMode(IdleMode.kBrake);

    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    if (m_autoSelected == kConeAuto) {
      autonomousIntakePower = INTAKE_OUTPUT_POWER;
    } else if (m_autoSelected == kCubeAuto) {
      autonomousIntakePower = -INTAKE_OUTPUT_POWER;
    }

    autonomousStartTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    if (m_autoSelected == kNothingAuto) {
      setArmMotor(0.0);
      setIntakeMotor(0.0, INTAKE_CURRENT_LIMIT_A);
      dt.setDt(0.0, 0.0);
      return;
    }

    double timeElapsed = Timer.getFPGATimestamp() - autonomousStartTime;

    if (timeElapsed < ARM_EXTEND_TIME_S) {
      setArmMotor(ARM_OUTPUT_POWER);
      setIntakeMotor(0.0, INTAKE_CURRENT_LIMIT_A);
      dt.setDt(0.0, 0.0);
    } else if (timeElapsed < ARM_EXTEND_TIME_S + AUTO_THROW_TIME_S) {
      setArmMotor(0.0);
      setIntakeMotor(autonomousIntakePower, INTAKE_CURRENT_LIMIT_A);
      dt.setDt(0.0, 0.0);
    } else if (timeElapsed < ARM_EXTEND_TIME_S + AUTO_THROW_TIME_S + ARM_EXTEND_TIME_S) {
      setArmMotor(-ARM_OUTPUT_POWER);
      setIntakeMotor(0.0, INTAKE_CURRENT_LIMIT_A);
      dt.setDt(0.0, 0.0);
    } else if (timeElapsed < ARM_EXTEND_TIME_S + AUTO_THROW_TIME_S + ARM_EXTEND_TIME_S + AUTO_DRIVE_TIME) {
      setArmMotor(0.0);
      setIntakeMotor(0.0, INTAKE_CURRENT_LIMIT_A);
      dt.setDt(AUTO_DRIVE_SPEED, 0.0);
    } else {
      setArmMotor(0.0);
      setIntakeMotor(0.0, INTAKE_CURRENT_LIMIT_A);
      dt.setDt(0.0, 0.0);
    }
  }

  /**
   * Used to remember the last game piece picked up to apply some holding power.
   */
  static final int CONE = 1;
  static final int CUBE = 2;
  static final int NOTHING = 3;
  int lastGamePiece;

  @Override
  public void teleopInit() {
    // driveLeftSpark.setIdleMode(IdleMode.kCoast);
    // driveLeftSparkTwo.setIdleMode(IdleMode.kCoast);
    // driveRightSpark.setIdleMode(IdleMode.kCoast);
    // driveRightSparkTwo.setIdleMode(IdleMode.kCoast);

    lastGamePiece = NOTHING;
  }

  @Override
  public void teleopPeriodic() {
    double armPower = -j.getRawAxis(5);
    /**if (j.getRawButton(6)) {
      // lower the arm
      armPower = -ARM_OUTPUT_POWER;
    } else if (j.getRawButton(5)) {
      // raise the arm
      armPower = ARM_OUTPUT_POWER;
    } else {
      // do nothing and let it sit where it is
      armPower = 0.0;
    }*/
    setArmMotor(armPower*dt.getSpeed().getSpeed());
  
    double intakePower;
    int intakeAmps;
    if (j.getRawButton(5)) {
      // cube in or cone out
      intakePower = INTAKE_OUTPUT_POWER;
      intakeAmps = INTAKE_CURRENT_LIMIT_A;
      lastGamePiece = CUBE;
    } else if (j.getRawButton(6)) {
      // cone in or cube out
      intakePower = -INTAKE_OUTPUT_POWER;
      intakeAmps = INTAKE_CURRENT_LIMIT_A;
      lastGamePiece = CONE;
    } /**else if (lastGamePiece == CUBE) {
      intakePower = INTAKE_HOLD_POWER;
      intakeAmps = INTAKE_HOLD_CURRENT_LIMIT_A;
    } else if (lastGamePiece == CONE) {
      intakePower = -INTAKE_HOLD_POWER;
      intakeAmps = INTAKE_HOLD_CURRENT_LIMIT_A;
    } */else {
      intakePower = 0.0;
      intakeAmps = 0;
    }
    setIntakeMotor(intakePower * dt.getSpeed().getSpeed(), INTAKE_CURRENT_LIMIT_A);

    System.out.println(dt.getController().getPositionTolerance());
    dt.setSetpoint(50);
    dt.periodic();
    dt.incrementSpeed(j.getPOV());
    SmartDashboard.putData("kSpeed ", dt.getSpeed());
    /*
     * Negative signs here because the values from the analog sticks are backwards
     * from what we want. Forward returns a negative when we want it positive.
     */

    SmartDashboard.putNumber("encoder", dt.getLeftCoder().getPosition().getValue());
    dt.setDt(j.getRawAxis(0), j.getRawAxis(3)-j.getRawAxis(2));
  }
}
