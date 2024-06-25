package frc.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.OI;
import frc.robot.subsystems.Arm;
import frc.robot.RobotMap;

public class Telemetry {
    // private NetworkTable table;
    private NetworkTableInstance inst;

    private NetworkTable main;

    private static NetworkTable _pivot;

    private NetworkTable _debug;

    private NetworkTable _controls;
    private NetworkTable _driver;

    private Arm pivot = Arm.getInstance();

    private XboxGamepad oiDriver = OI.getInstance().getDriver();

    public Telemetry() {
        // main = NetworkTableInstance.getDefault();
        inst = NetworkTableInstance.getDefault();

        main = inst.getTable("TEAM 1072");

        // _realOutputs = main.getSubTable("Real Outputs");

        _pivot = main.getSubTable("Pivot");

        _debug = main.getSubTable("Debug");
        _controls = _debug.getSubTable("Controls");
        _driver = _controls.getSubTable("Driver");
    }

    public void debug() {
        NetworkTableEntry a_D = _driver.getEntry("Button A");
        a_D.setBoolean(oiDriver.getButtonAState());

        NetworkTableEntry b_D = _driver.getEntry("Button B");
        b_D.setBoolean(oiDriver.getButtonBState());

        NetworkTableEntry x_D = _driver.getEntry("Button X");
        x_D.setBoolean(oiDriver.getButtonXState());

        NetworkTableEntry y_D = _driver.getEntry("Button Y");
        y_D.setBoolean(oiDriver.getButtonYState());

        NetworkTableEntry leftBumper_D = _driver.getEntry("Left Bumper");
        leftBumper_D.setBoolean(oiDriver.getLeftBumperState());

        NetworkTableEntry rightBumper_D = _driver.getEntry("Right Bumper");
        rightBumper_D.setBoolean(oiDriver.getRightBumperState());

        NetworkTableEntry leftTrigger_D = _driver.getEntry("Left Trigger");
        leftTrigger_D.setDouble(oiDriver.getLeftTrigger());

        NetworkTableEntry rightTrigger_D = _driver.getEntry("Right Trigger");
        rightTrigger_D.setDouble(oiDriver.getRightTrigger());

        NetworkTableEntry upD_D = _driver.getEntry("Up DPad");
        upD_D.setBoolean(oiDriver.getUpDPadButtonState());

        NetworkTableEntry leftD_D = _driver.getEntry("Left DPad");
        leftD_D.setBoolean(oiDriver.getLeftDPadButtonState());

        NetworkTableEntry downD_D = _driver.getEntry("Down DPad");
        downD_D.setBoolean(oiDriver.getDownDPadButtonState());

        NetworkTableEntry rightD_D = _driver.getEntry("Right DPad");
        rightD_D.setBoolean(oiDriver.getDownDPadButtonState());

        NetworkTableEntry select_D = _driver.getEntry("Select");
        select_D.setBoolean(oiDriver.getButtonSelectState());

        NetworkTableEntry start_D = _driver.getEntry("Start");
        start_D.setBoolean(oiDriver.getButtonStartState());
    }

    public void pivot() {

        NetworkTableEntry pivotSensorPosition = _pivot.getEntry("Pivot Sensor Position");
        pivotSensorPosition.setDouble(pivot.getPosition());

        NetworkTableEntry pivotSensorVelocity = _pivot.getEntry("Pivot Sensor Velocity");
        pivotSensorVelocity.setDouble(pivot.getVelocity());

        NetworkTableEntry pivotVelocity = _pivot.getEntry("Pivot Velocity");
        pivotVelocity.setDouble(pivot.getVelocity());

        NetworkTableEntry setPivotAngle = _pivot.getEntry("Pivot Angle");
        setPivotAngle.setDouble(pivot.getPivotSetpoint(0));

        NetworkTableEntry pivotkP = _pivot.getEntry("Pivot kP");
        // pivotkP.setDouble(RobotMap.Arm.PIVOT_kP);

        NetworkTableEntry pivotkI = _pivot.getEntry("Pivot kI");
        // pivotkI.setDouble(RobotMap.Arm.PIVOT_kI);

        NetworkTableEntry pivotkD = _pivot.getEntry("Pivot kD");
        // pivotkD.setDouble(RobotMap.Arm.PIVOT_kD);

        NetworkTableEntry isStalling = _pivot.getEntry("Pivot Is Stalling");
        isStalling.setBoolean(Arm.getInstance().isStalling());
        
        NetworkTableEntry pivotCurrent = _pivot.getEntry("Pivot Current");
        pivotCurrent.setDouble(Arm.getInstance().getOutputCurrent());

        NetworkTableEntry cmdBool = _pivot.getEntry("Schedule Command");
        cmdBool.setBoolean(RobotMap.Arm.scheduleCmd);
    }

    public static void putNumber(String system, String entry, double number) {
        switch (system) {
            case "pivot":
                NetworkTableEntry _entryPivot = _pivot.getEntry(entry);
                _entryPivot.setDouble(number);
                break;
            default:
                break;
        }
    }

    public static void putBoolean(String system, String entry, boolean bool) {
        switch (system) {
            case "pivot":
                NetworkTableEntry _entryPivot = _pivot.getEntry(entry);
                _entryPivot.setBoolean(bool);
                break;
            default:
                break;
        }
    }

    public static void putString(String system, String entry, String string) {
        switch (system) {
            case "pivot":
                NetworkTableEntry _entryPivot = _pivot.getEntry(entry);
                _entryPivot.setString(string);
                break;
            default:
                break;
        }
    }

    public void publish() {
        debug();
        pivot();

        inst.flushLocal();
        inst.flush();
    }
}