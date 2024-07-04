package frc.robot.subsystems;

import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.pivot.ZeroPivot;
import frc.robot.util.Telemetry;

public class Arm extends PIDSubsystem {
    private static Arm instance; 
    
    private CANSparkMax master; 

    private CANcoder leftCoder;

    private ZeroPivot zeroCmd;

    // Mutable holder for unit-safe voltage values, persisted to avoid reallocation.
    private final MutableMeasure<Voltage> _appliedVoltage = mutable(Volts.of(0));
    // Mutable holder for unit-safe linear distance values, persisted to avoid
    // reallocation.
    private final MutableMeasure<Angle> _angle = mutable(Degrees.of(0));
    // Mutable holder for unit-safe linear velocity values, persisted to avoid
    // reallocation.
    private final MutableMeasure<Velocity<Angle>> _velocity = mutable(DegreesPerSecond.of(0));

    private Arm() {
        
        super(new PIDController(0, 0, 0));

        master = new CANSparkMax(RobotMap.Arm.MASTER_ID, CANSparkLowLevel.MotorType.kBrushed);
        
        leftCoder = new CANcoder(RobotMap.Arm.CANCODER_ID, "rio");

        // speakerAngles = new InterpolatingDoubleTreeMap();
        // speakerAngles.put(0.0, 0.0); // TODO

        getController().setTolerance(0.1, 0.0);

        configMotors();
    }
    
    private void configMotors() {

        
        // master.enableSoftLimit(CANSparkBase.SoftLimitDirection.kForward, true);
        // master.enableSoftLimit(CANSparkBase.SoftLimitDirection.kReverse, true);

        // master.setSoftLimit(CANSparkBase.SoftLimitDirection.kForward, (float) (RobotMap.Arm.PIVOT_FORWARD_SOFT_LIMIT / 360.0));
        // master.setSoftLimit(CANSparkBase.SoftLimitDirection.kReverse, (float) (RobotMap.Arm.PIVOT_REVERSE_SOFT_LIMIT / 360.0));


        master.setIdleMode(IdleMode.kBrake); //TODO set current/voltage limits + soft position limits
        master.setInverted(RobotMap.Arm.MASTER_INVERT);

        master.setSecondaryCurrentLimit(RobotMap.Arm.CURRENT_LIMIT);

        // master.clearStickyFaults();

        // TalonFXConfiguration masterConfig = new TalonFXConfiguration();

        // masterConfig.MotorOutput.Inverted = RobotMap.Pivot.MASTER_INVERT;

        // masterConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        
        // masterConfig.Feedback.SensorToMechanismRatio = RobotMap.Pivot.PIVOT_GEAR_RATIO;

        // masterConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = RobotMap.Pivot.PIVOT_FORWARD_SOFT_LIMIT;
        // masterConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold = RobotMap.Pivot.PIVOT_REVERSE_SOFT_LIMIT;
        // masterConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        // masterConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

        // masterConfig.Slot0.kP = RobotMap.Pivot.PIVOT_kP;
        // // masterConfig.Slot0.kS = RobotMap.Pivot.PIVOT_kS;
        // // masterConfig.Slot0.kV = RobotMap.Pivot.PIVOT_kV;
        // // masterConfig.Slot0.kA = RobotMap.Pivot.PIVOT_kA;
        // masterConfig.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        // masterConfig.Slot0.kG = RobotMap.Pivot.PIVOT_kG;

        // masterConfig.MotionMagic.MotionMagicCruiseVelocity = RobotMap.Pivot.MAX_CRUISE_VElOCITY;
        // masterConfig.MotionMagic.MotionMagicAcceleration = RobotMap.Pivot.MAX_CRUISE_ACCLERATION;

        // master.getConfigurator().apply(masterConfig);


    }

    public double getOutputCurrent() {
        return master.getOutputCurrent();
    }

    /*
     * Get pivot angle in degrees
     */
    public double getPosition() {
        return getMeasurement() * RobotMap.Arm.PIVOT_ROT_TO_ANGLE;
    }

    @Override
    public void periodic() {
        
    }


    public boolean isStalling() {
        return master.getOutputCurrent() > RobotMap.Arm.STALLING_CURRENT;
    }

    /*
     * Get pivot angle in degrees per second
     */
    public double getVelocity() {
        return leftCoder.getVelocity().getValue() * RobotMap.Arm.PIVOT_ROT_TO_ANGLE;
    }

    public double getVoltage() {
        return master.getBusVoltage() * master.getAppliedOutput();
    }

    public void moveToPosition(double angle) {
        getController().setSetpoint(angle);
        
        useOutput(getController().calculate(getPosition()), angle);
    }

    @Override
    protected void useOutput(double output, double setpoint) { // manual voltage limiting and manual forward soft limit
        Telemetry.putNumber("pivot", "Output", output);
        if (!getInstance().isStalling() && isValidOutput(output)) {
            if(output > RobotMap.Arm.VOLTAGE_LIMIT) {
                master.setVoltage(RobotMap.Arm.VOLTAGE_LIMIT);
            } else if (output < -RobotMap.Arm.VOLTAGE_LIMIT) {
                master.setVoltage(-RobotMap.Arm.VOLTAGE_LIMIT);
            } else {
                master.setVoltage(output);
            }
        } else {
            master.setVoltage(0.0);
        }
    }

    @Override
    protected double getMeasurement() {
        return leftCoder.getPosition().getValue() * RobotMap.Arm.CANCODER_INVERT;
    }
    
    public void setUseOutput(double output, double setpoint) {
        useOutput(output, setpoint);
    }

    public void setPercentOutput(double power, boolean override) {
        if (override || isValidOutput(power)) {
            master.set(power);
        } else {
            master.set(0.0);
        }
        
    }

    private boolean isValidOutput(double output) {
        return (getInstance().getPosition() < RobotMap.Arm.PIVOT_FORWARD_SOFT_LIMIT && 
        getInstance().getPosition() > RobotMap.Arm.PIVOT_REVERSE_SOFT_LIMIT) ||
        (getInstance().getPosition() >= RobotMap.Arm.PIVOT_FORWARD_SOFT_LIMIT && output < 0) ||
        (getInstance().getPosition() <= RobotMap.Arm.PIVOT_REVERSE_SOFT_LIMIT && output > 0);
    }

    public void setSensorPosition(double degrees) {
        leftCoder.getConfigurator().setPosition(degrees);
    }

    public double getPivotSetpoint(double distance) {
        return 0;
    }

    public static Arm getInstance() {
        if(instance == null) {
            instance = new Arm();
        }
        return instance; 
    }
}
