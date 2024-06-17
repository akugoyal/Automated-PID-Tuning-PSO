package frc.robot.commands.drivetrain;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.swerve.Drivetrain;
import frc.robot.OI;
import frc.robot.util.Flip;
import frc.robot.util.MathUtil;


public class SwerveManual extends Command {
    private double vx, vy, prevvx, prevvy, omega;

    private SlewRateLimiter vxFilter, vyFilter;

    public SwerveManual() {
        addRequirements(Drivetrain.getInstance());
        vx = 0;
        vy = 0;
        prevvx = 0;
        prevvy = 0;
        omega = 0;
        vxFilter = new SlewRateLimiter(RobotMap.Drivetrain.MAX_ACCELERATION);
        vyFilter = new SlewRateLimiter(RobotMap.Drivetrain.MAX_ACCELERATION);
    }
     public void execute() {
        // set previous x and y velocities
        prevvx = vx;
        prevvy = vy;

        // get x, y, and rotational velocities from joystick
        vx =
            MathUtil.mapJoystickOutput(
                OI.getInstance().getDriver().getLeftY(), RobotMap.OI.JOYSTICK_DEADBAND);
        vy =
            MathUtil.mapJoystickOutput(
                -OI.getInstance().getDriver().getLeftX(), RobotMap.OI.JOYSTICK_DEADBAND);
        omega =
            -MathUtil.mapJoystickOutput(
                OI.getInstance().getDriver().getRightX(), RobotMap.OI.JOYSTICK_DEADBAND);
        
        if (Flip.isFlipped())
        {
            vx *= -1;
            vy *= -1;
        }
                // Scaling velocities based on multipliers
        vx = scaleValues(vx, RobotMap.Drivetrain.MAX_DRIVING_SPEED); //*(RobotMap.SwerveManual.SPEED_MULTIPLIER);
        vy = scaleValues(vy, RobotMap.Drivetrain.MAX_DRIVING_SPEED) ;//* (RobotMap.SwerveManual.SPEED_MULTIPLIER);
        omega = scaleValues(omega, RobotMap.Drivetrain.MAX_ANGLE_VELOCITY); //* ( RobotMap.SwerveManual.SPEED_MULTIPLIER);

        omega = Drivetrain.getInstance().adjustPigeon(omega);

        // limits acceleration
        vy = vyFilter.calculate(vy); // limitAccelration(vy, prevvy);
        vx = vxFilter.calculate(vx); // limitAcceleration(vx, prevvx);

        // aligns to speaker
        if (OI.getInstance().getDriver().getRightBumperState()) {
            omega = Drivetrain.getInstance().alignToSpeaker();
            Drivetrain.getInstance().setPreviousHeading(-Drivetrain.getInstance().getPoseEstimatorPose2d().getRotation().getDegrees());
        }

        // aligns to amp
        if (OI.getInstance().getDriver().getLeftBumperState()) {
            vx = -Drivetrain.getInstance().alignToAmp()[0];
            omega = Drivetrain.getInstance().alignToAmp()[1];
        }

        // if rotational velocity is very small
        if (Math.abs(omega) < RobotMap.Drivetrain.MIN_OUTPUT) {
            omega = 0.0001;
        }

        // sets velocities to zero if robot is not visibly moving
        if (isRobotStill()) {
            vx = 0;
            vy = 0;
        }
        
        if (Drivetrain.getInstance().robotCentric())
            Drivetrain.getInstance()
                .setAngleAndDrive(
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                        vx, vy, -omega, Rotation2d.fromDegrees(0)));
        else
            Drivetrain.getInstance()
                .setAngleAndDrive(
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                        vx, vy, -omega, Drivetrain.getInstance().getRotation()));
    }

    /**
     * Limits the drivetrain's acceleration
     * @param value     velocity to correct
     * @param prevValue previous velocity
     * @return          corrected velocity
     */
    private double limitAcceleration(double value, double prevValue) {
        if (Math.abs(value - prevValue) / RobotMap.ROBOT_LOOP > RobotMap.Drivetrain.MAX_ACCELERATION) {
            value = prevValue + Math.signum(value - prevValue)
                    * (RobotMap.Drivetrain.MAX_ACCELERATION)
                    * RobotMap.ROBOT_LOOP;
            // previous velocity + direction of movement (+/-) * acceleration * time (a=v/t)
        }
        return value;
    }

    /**
     * Scales the velocities by their given multiplier
     * @param value         velocity to scale
     * @param scaleFactor   multiplier
     * @return              scaled velocity
     */
    private double scaleValues(double value, double scaleFactor) {
        return value * scaleFactor;
    }

    /**
     * @return if the robot is moving slow enough for it to be considered still
     */
    private boolean isRobotStill() {
        return Math.sqrt(vx * vx + vy * vy) < RobotMap.Drivetrain.MIN_OUTPUT;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * Sets the x, y, and rotational velocities to 0.
     */
    public void end(boolean interrupted) {
        Drivetrain.getInstance().setAngleAndDrive(new ChassisSpeeds());
    }
}
