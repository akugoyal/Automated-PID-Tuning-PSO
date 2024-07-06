package frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.PSO.*;

public class RobotMap {

    // Global Robot Constants
    public static final boolean FIRST_BOT = true;
    public static final double ROBOT_LOOP = 0.02;
    public static final String CAN_CHAIN = "rio";

    public static final class PSO {
        public static final double TEST_LENGTH = 5;
        public static Particle[] particles;
        public static final int NUM_PARTICLES = 20;
    }

    public static final class OI {
        public static final double JOYSTICK_DEADBAND = 0.15;
        public static final double TRIGGER_DEADBAND = 0.1;
      
        public static final int DRIVER_ID = 0;
    }

    public static class Arm {
        public static final int MASTER_ID = 9;
        public static final int CANCODER_ID = 37;

        
        public static final String LOAD_FILE = "/U/savefileB_contcontcont2.txt"; //TODO change to wanted file
        public static final String SAVE_FILE_HEADER = "/U/savefileB_contcontcontcont"; //use case: since it saves every epoch, if string was save, savefiles would be save1.txt, save2.txt, etc.

        public static final boolean SAVE_SWARM = true;
        public static final boolean LOAD_SWARM = true;

        public static final int CANCODER_INVERT = -1;

        public static final boolean MASTER_INVERT = true;
        
        public static final double ZERO_SPEED = -0.4; //$$

        // public static double PIVOT_kP = 0.07250318043022665;
        // public static double PIVOT_kI = 0.10716938060041942;
        // public static double PIVOT_kD = 4.4820412815789034E-4;

        public static boolean scheduleCmd = false;
        public static Command cmdToSchedule;

        public static final double PIVOT_GEAR_RATIO = 128;
        public static final double PIVOT_ROT_TO_ANGLE = 360; // motor rotations to degrees

        public static final double STALLING_CURRENT = 30; //$$
        
        public static final double MAX_ERROR = 1; // $$

        public static final double CURRENT_LIMIT = 40.0;
        public static final double VOLTAGE_LIMIT = 3.0; //TODO
    
        public static final double PIVOT_FORWARD_SOFT_LIMIT = 120; //$$
        public static final double PIVOT_REVERSE_SOFT_LIMIT = 0; //$$

        public static String[][] loadFiles;
    }
}