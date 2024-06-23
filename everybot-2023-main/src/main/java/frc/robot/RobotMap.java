package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import edu.wpi.first.math.util.Units;

public class RobotMap {

    // Global Robot Constants
    public static final boolean FIRST_BOT = true;
    public static final double ROBOT_LOOP = 0.02;
    public static final String CAN_CHAIN = "rio";

    public static final class OI {
        public static final double JOYSTICK_DEADBAND = 0.15;
        public static final double TRIGGER_DEADBAND = 0.1;
      
        public static final int DRIVER_ID = 0;
    }

    public static final class Drivetrain {

        public static final double MIN_OUTPUT = 0.05;
        
        // Robot Dimensions
        public static final double ROBOT_LENGTH = Units.inchesToMeters(28);
        public static final double ROBOT_WIDTH = Units.inchesToMeters(28);

        public static final double MAX_DRIVING_SPEED = 5.0; // m/s //TODO
        public static final double MAX_ACCELERATION = 7;

        // Speed multipliers
        public static final double SPEED_MULTIPLIER = 1.0; // TODO
        public static final double ROT_MULITPLIER = 1.0; // TODO
        public static final double CLAMP_MULTIPLIER = 0.7;
    }

    public static class Arm {
        public static final int MASTER_ID = 9;
        public static final int CANCODER_ID = 37;

        public static final boolean MASTER_INVERT = true;
        
        public static final double ZERO_SPEED = -0.5; //$$

        public static double PIVOT_kP = 1; //TODO implement access/modify functions
        public static double PIVOT_kI = 0;
        public static double PIVOT_kD = 0;

        public static final double PIVOT_GEAR_RATIO = 64;
        public static final double PIVOT_ROT_TO_ANGLE = 360; // motor rotations to degrees

        public static final double STALLING_CURRENT = 40; //$$
        
        public static final double MAX_ERROR = 1; // $$
    
        public static final double PIVOT_FORWARD_SOFT_LIMIT = 65; //$$
        public static final double PIVOT_REVERSE_SOFT_LIMIT = 0; //$$

        public static enum Goal {
            ZERO
        }
    }
}