package frc.robot;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class Speed implements Sendable{
    private double kSpeed;

    public Speed() {
        kSpeed = 1;
    }

    public void setSpeed(double s) {
        kSpeed = s;
    }
    
    public double getSpeed() {
    return kSpeed;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
      builder.addDoubleProperty("kSpeed", this::getSpeed, this::setSpeed);
      builder.setSmartDashboardType("testing");
      builder.update();
    }
}
