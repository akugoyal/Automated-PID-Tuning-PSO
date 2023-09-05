package frc.robot;
import com.ctre.phoenix6.hardware.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {
    private CANSparkMax driveLeftSpark = new CANSparkMax(7, MotorType.kBrushed);
    private CANSparkMax driveRightSpark = new CANSparkMax(5, MotorType.kBrushed);
    private CANSparkMax driveLeftSparkTwo = new CANSparkMax(6, MotorType.kBrushed);
    private CANSparkMax driveRightSparkTwo = new CANSparkMax(8, MotorType.kBrushed);
    private CANcoder leftCoder = new CANcoder(35, "rio");
    static Speed dtSpeed = new Speed();
    static boolean changeSpeed = false;

    /**
   * Calculate and set the power to apply to the left and right
   * drive motors.
   * 
   * @param turn Desired forward speed. Positive is forward.
   * @param forward    Desired turning speed. Positive is counter clockwise from
   *                above.
   */
  public void setDriveMotors(double turn, double forward) {
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
}
