import java.awt.*;

public enum EnemyType {
    SCOUT(3.0, 2.0, 1, Color.BLUE, 40, 2),      // Fast, weak, shoots frequently with low damage
    FIGHTER(2.0, 4.0, 2, Color.RED, 50, 3),    // Balanced stats
    HEAVY(1.0, 8.0, 4, Color.ORANGE, 60, 5),   // Slow, tanky, shoots slowly with high damage
    SNIPER(1.5, 5.0, 3, Color.MAGENTA, 90, 8);   // Slow shooter, high damage

    private final double speed;
    private final Color tint;
    private final double health;
    private final int points;
    private final int fireDelay;
    private final double damage;

    EnemyType(double speed, double health, int points, Color tint, int fireDelay, double damage) {
        this.speed = speed;
        this.health = health;
        this.points = points;
        this.tint = tint;
        this.fireDelay = fireDelay;
        this.damage = damage;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHealth() {
        return health;
    }

    public int getPoints() {
        return points;
    }

    public Color getTint() {
        return tint;
    }

    public int getFireDelay() {
        return fireDelay;
    }

    public double getDamage() {
        return damage;
    }
}