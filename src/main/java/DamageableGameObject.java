import java.awt.*;

/**
 * Abstract base class for game objects that have health and can take damage. <br>
 * Extends {@link GameObject} and adds health management.
 */
public abstract class DamageableGameObject extends GameObject {
    /**
     * The current health of this object.
     */
    private double health;

    /**
     * The maximum health capacity of this object.
     */
    private double maxHealth;

    /**
     * Constructs a damageable game object with specified size, position, and maximum health. <br>
     * Health is initialized to the maximum value.
     *
     * @param size the size of the object
     * @param position the initial position of the object
     * @param maxHealth the maximum health capacity
     */
    public DamageableGameObject(Vector size, Vector position, double maxHealth) {
        super(size, position);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public void act(GameContext context) {
        super.act(context);
        // If this object is dead, remove it from the game
        if (isDead()) {
            context.removeActor(this);
        }
    }

    /**
     * Applies damage to this object, reducing its health. <br>
     * Health cannot go below zero.
     *
     * @param damage the amount of damage to apply
     */
    public void damage(double damage) {
        health = Math.max(0, health - damage);
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealth() {
        return health;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Checks if this object is dead.
     *
     * @return true if health is zero or below, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * A helper method to draw a health bar. <br>
     * The bar is colored according to the object's faction and fills proportionally to remaining health.
     *
     * @param g the Graphics object used for drawing
     * @param x the x-coordinate of the health bar
     * @param y the y-coordinate of the health bar
     * @param width the total width of the health bar
     * @param height the height of the health bar
     */
    protected void drawHealthBar(Graphics g, int x, int y, int width, int height) {
        g.setColor(getFaction().getColor());
        g.fillRect(x, y, (int) (width * (health / maxHealth)), height);
    }
}