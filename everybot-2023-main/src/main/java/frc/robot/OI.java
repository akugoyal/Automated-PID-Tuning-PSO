package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.pivot.PivotToAngleTimed;
import frc.robot.commands.pivot.ZeroPivot;
import frc.robot.subsystems.Arm;
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
        // driver.getButtonB().onTrue(new ZeroPivot());
        // driver.getButtonA().whileTrue(new PivotToAngle(RobotMap.Arm.Goal.ZERO));
        // driver.getButtonA().onTrue(new PivotToAngleTimed(30));
        // driver.getButtonY().onTrue(new PivotToAngleTimed(60));
        // driver.getButtonX().onTrue(new PivotToAngleTimed(90));
        // driver.getRightDPadButton().onTrue(new PivotToAngleTimed(15));
        // driver.getLeftDPadButton().onTrue(new PivotToAngleTimed(45));
        // driver.getRightBumper().onTrue(new PivotToAngleTimed(75));
        // driver.getLeftBumper().onTrue(new PivotToAngleTimed(105));
        // driver.getUpDPadButton().whileTrue(new InstantCommand(() -> Arm.getInstance().setUseOutput(3.0, 0.0)));
        // driver.getUpDPadButton().whileFalse(new InstantCommand(() -> Arm.getInstance().setPercentOutput(0, false)));
        // driver.getButtonA().whileFalse(new InstantCommand(() -> Arm.getInstance().setPercentOutput(0)));
    }

    public static OI getInstance() {
        if (instance == null)
            instance = new OI();
        return instance;
    }
}