package frc.robot;
import com.ctre.phoenix6.hardware.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class Drivetrain extends PIDSubsystem{
    private CANSparkMax driveLeftSpark = new CANSparkMax(7, MotorType.kBrushed);
    private CANSparkMax driveRightSpark = new CANSparkMax(5, MotorType.kBrushed);
    private CANSparkMax driveLeftSparkTwo = new CANSparkMax(6, MotorType.kBrushed);
    private CANSparkMax driveRightSparkTwo = new CANSparkMax(8, MotorType.kBrushed);
    private CANcoder leftCoder = new CANcoder(35, "rio");
    //private CANcoder rightCoder = new CANcoder(36, "rio");
    private static Speed dtSpeed;
    private static boolean changeSpeed;
    private static final double kP= 2.35;
    private static final double kI= 0.25;
    private static final double kD= 0.05;

    public Drivetrain() {
        super(new PIDController(kP, kI, kD));
        enable();
        getController().setTolerance(0.1, 0.2);
        driveLeftSpark.setIdleMode(IdleMode.kBrake);
        driveLeftSparkTwo.setIdleMode(IdleMode.kBrake);
        driveRightSpark.setIdleMode(IdleMode.kBrake);
        driveRightSparkTwo.setIdleMode(IdleMode.kBrake);
        driveLeftSpark.setInverted(false);
        driveLeftSparkTwo.setInverted(false);
        driveRightSpark.setInverted(false);
        driveRightSparkTwo.setInverted(false);
        dtSpeed = new Speed(1, 0.1);
        changeSpeed = true;
    }

    /**
   * Calculate and set the power to apply to the left and right
   * drive motors.
   * 
   * @param turn Desired forward speed. Positive is forward.
   * @param forward    Desired turning speed. Positive is counter clockwise from
   *                above.
   */
  private void setDriveMotors(double turn, double forward) {
    if (turn == 0 && forward == 0) {
      driveLeftSpark.setIdleMode(IdleMode.kBrake);
      driveLeftSparkTwo.setIdleMode(IdleMode.kBrake);
      driveRightSpark.setIdleMode(IdleMode.kBrake);
      driveRightSparkTwo.setIdleMode(IdleMode.kBrake);
    }
    SmartDashboard.putNumber("drive forward power (%)", forward);
    SmartDashboard.putNumber("drive turn power (%)", turn);

    /*
     * positive turn = counter clockwise, so the left side goes backwards
     */
    double left = turn - forward;
    double right = turn + forward;

    SmartDashboard.putNumber("drive left power (%)", left);
    SmartDashboard.putNumber("drive right power (%)", -right);

    // see note above in robotInit about commenting these out one by one to set
    // directions.
    driveLeftSpark.set(left);
    driveLeftSparkTwo.set(left);
    driveRightSpark.set(right);
    driveRightSparkTwo.set(right);
    }

    public boolean incrementSpeed(int POV) {
        if (POV == -1) {
            changeSpeed = true;
        }
        else if (POV == 0 && changeSpeed) {
            dtSpeed.increaseSpeed();
            changeSpeed = false;
        } else if (POV == 180 && changeSpeed) {
            dtSpeed.decreaseSpeed();
            changeSpeed = false;
        }
        return true;
    }

    public void setDt(double turn, double forward) {
        double frwd = forward * forward;
        if (forward < 0) {
            frwd *= -1;
        }
        setDriveMotors(-0.5 * turn * dtSpeed.getSpeed(), -frwd * dtSpeed.getSpeed());
    }

    public Speed getSpeed() {
        return dtSpeed;
    }

    @Override
    protected void useOutput(double output, double setpoint) {
        // TODO Auto-generated method stub
        SmartDashboard.putNumber("output", output);
        if (!getController().atSetpoint()) {
            setDriveMotors(output, 0);
        }
        //driveLeftSparkTwo.setVoltage(output);
    }

    @Override
    protected double getMeasurement() {
        return leftCoder.getPosition().getValue();
    }

    public CANcoder getLeftCoder() {
        return leftCoder;
    }
}
