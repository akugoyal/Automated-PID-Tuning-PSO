package frc.robot.subsystems;

import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
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
import frc.robot.RobotMap;
import frc.robot.util.Telemetry;

public class Arm extends PIDSubsystem {
    private static Arm instance; 
    
    private CANSparkMax master; 

    private CANcoder leftCoder;

    private DigitalInput limitSwitch;

    private InterpolatingDoubleTreeMap speakerAngles;
    // Mutable holder for unit-safe voltage values, persisted to avoid reallocation.
    private final MutableMeasure<Voltage> _appliedVoltage = mutable(Volts.of(0));
    // Mutable holder for unit-safe linear distance values, persisted to avoid
    // reallocation.
    private final MutableMeasure<Angle> _angle = mutable(Degrees.of(0));
    // Mutable holder for unit-safe linear velocity values, persisted to avoid
    // reallocation.
    private final MutableMeasure<Velocity<Angle>> _velocity = mutable(DegreesPerSecond.of(0));

    private Arm() {
        
        super(new PIDController(RobotMap.Arm.PIVOT_kP, RobotMap.Arm.PIVOT_kI, RobotMap.Arm.PIVOT_kD));

        master = new CANSparkMax(RobotMap.Arm.MASTER_ID, CANSparkLowLevel.MotorType.kBrushed);
        
        leftCoder = new CANcoder(RobotMap.Arm.CANCODER_ID, "rio");

        // speakerAngles = new InterpolatingDoubleTreeMap();
        // speakerAngles.put(0.0, 0.0); // TODO

        getController().setTolerance(0.1, 0.2);

        configMotors();
    }
    
    private void configMotors() {

        
        master.setIdleMode(IdleMode.kBrake);
        master.setInverted(false);

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

    /*
     * Get pivot angle in degrees
     */
    public double getPosition() {
        return leftCoder.getPosition().getValue() * RobotMap.Arm.PIVOT_ROT_TO_ANGLE;
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
        useOutput(getController().calculate(getPosition()), angle);
    }

    @Override
    protected void useOutput(double output, double setpoint) {
        master.setVoltage(output);
    }

    @Override
    protected double getMeasurement() {
        return leftCoder.getPosition().getValue();
    }
    
    public void setPercentOutput(double power) {
        master.set(power);
    }

    public void setSensorPosition(double degrees) {
        leftCoder.getConfigurator().setPosition(degrees);
    }

    public boolean isLimitHit() {
        return !limitSwitch.get();
    }

    public double getPivotSetpoint(double distance) {
        return speakerAngles.get(distance);
    }

    private final SysIdRoutine _sysId = new SysIdRoutine(
            new SysIdRoutine.Config(),
            new SysIdRoutine.Mechanism(
                    (Measure<Voltage> volts) -> {
                        master.setVoltage(volts.in(Volts));
                    },
                    log -> {
                        log.motor("pivot-master")
                                .voltage(
                                        _appliedVoltage.mut_replace(
                                               getVoltage(), Volts))
                                .angularPosition(
                                        _angle.mut_replace(getPosition(), Degrees))
                                .angularVelocity(
                                        _velocity.mut_replace(getVelocity(), DegreesPerSecond));
                    },
                    this));

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return _sysId.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return _sysId.dynamic(direction);
    }

    public static Arm getInstance() {
        if(instance == null) {
            instance = new Arm();
        }
        return instance; 
    }
}