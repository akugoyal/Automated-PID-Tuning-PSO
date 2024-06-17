package frc.robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.pivot.ZeroPivot;
import frc.robot.subsystems.Pivot;
import frc.robot.util.XboxGamepad;

public class OI {
    private static OI instance;

    private XboxGamepad driver;

    public OI() {
        driver = new XboxGamepad(RobotMap.OI.DRIVER_ID);
        initBindings();
    }

    public XboxGamepad getDriver() {
        return driver;
    }

    private void initBindings() {
        driver.getButtonB().whileTrue(new ZeroPivot());
        // driver.getButtonA().whileTrue(new PivotToAngle(RobotMap.Arm.Goal.ZERO));
        driver.getButtonA().whileTrue(new InstantCommand(() -> Pivot.getInstance().setPercentOutput(1)));
        driver.getButtonA().whileFalse(new InstantCommand(() -> Pivot.getInstance().setPercentOutput(0)));
    }

    public static OI getInstance() {
        if (instance == null)
            instance = new OI();
        return instance;
    }
}