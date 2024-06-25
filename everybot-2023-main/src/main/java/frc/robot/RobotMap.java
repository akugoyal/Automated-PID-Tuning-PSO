package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import edu.wpi.first.math.util.Units;

public class RobotMap {

    // Global Robot Constants
    public static final boolean FIRST_BOT = true;
    public static final double ROBOT_LOOP = 0.02;
    public static final String CAN_CHAIN = "rio";

    public static final class PSO {
        public static final double TEST_LENGTH = 15.5;
    }

    public static final class OI {
        public static final double JOYSTICK_DEADBAND = 0.15;
        public static final double TRIGGER_DEADBAND = 0.1;
      
        public static final int DRIVER_ID = 0;
    }

    public static class Arm {
        public static final int MASTER_ID = 9;
        public static final int CANCODER_ID = 37;

        public static final int CANCODER_INVERT = -1;

        public static final boolean MASTER_INVERT = true;
        
        public static final double ZERO_SPEED = -0.3; //$$

        public static double PIVOT_kP = 0; //TODO implement access/modify functions
        public static double PIVOT_kI = 0;
        public static double PIVOT_kD = 0;

        public static final double PIVOT_GEAR_RATIO = 64;
        public static final double PIVOT_ROT_TO_ANGLE = 360; // motor rotations to degrees

        public static final double STALLING_CURRENT = 33; //$$
        
        public static final double MAX_ERROR = 1; // $$

        public static final double CURRENT_LIMIT = 40.0;
        public static final double VOLTAGE_LIMIT = 2.0; //TODO
    
        public static final double PIVOT_FORWARD_SOFT_LIMIT = 120; //$$
        public static final double PIVOT_REVERSE_SOFT_LIMIT = 0; //$$

        public static enum Goal {
            ZERO,
            SETPOINT1,
            SETPOINT2,
            SETPOINT3
        }
    }
}