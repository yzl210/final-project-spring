import java.awt.*;

/**
 * Defines the different types of enemy planes with their stat profiles. <br>
 * Each type has unique speed, health, damage, fire rate, visual tint, and point value.
 */
public enum EnemyType {
    /**
     * Fast, weak enemy that shoots frequently with low damage. <br>
     * Blue tint, 1 point value.
     */
    SCOUT(3.0, 2.0, 1, Color.BLUE, 40, 2),

    /**
     * Balanced enemy with moderate stats across all categories. <br>
     * Red tint, 2 point value.
     */
    FIGHTER(2.0, 4.0, 2, Color.RED, 50, 3),

    /**
     * Slow, tanky enemy that shoots slowly with high damage. <br>
     * Orange tint, 4 point value.
     */
    HEAVY(1.0, 8.0, 4, Color.ORANGE, 60, 5),

    /**
     * Slow-firing enemy with very high damage per shot. <br>
     * Magenta tint, 3 point value.
     */
    SNIPER(1.5, 5.0, 3, Color.MAGENTA, 90, 8);

    /**
     * The base speed of this enemy type.
     */
    private final double speed;

    /**
     * The visual tint color for this enemy type.
     */
    private final Color tint;

    /**
     * The base health of this enemy type.
     */
    private final double health;

    /**
     * The point value awarded for killing this enemy type.
     */
    private final int points;

    /**
     * The fire delay in frames between shots.
     */
    private final int fireDelay;

    /**
     * The bullet damage dealt by this enemy type.
     */
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