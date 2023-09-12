package frc.robot;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class Speed implements Sendable{
    private double kSpeed;
    private double increment;

    public Speed() {
        kSpeed = 1;
    }

    public Speed(double speed) {
        kSpeed = speed;
    }

    public Speed(double speed, double inc) {
        kSpeed = speed;
        increment = inc;
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

    public void increaseSpeed() {
        kSpeed = Math.min(getSpeed() + increment, 1);
    }

    public void decreaseSpeed() {
        kSpeed = Math.max(getSpeed() - increment, 0);
    }
}
