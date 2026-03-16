import java.awt.*;

/**
 * Represents a projectile fired by a plane in the game. <br>
 * Bullets can damage actors of different factions.
 */
public class Bullet extends GameObject {
    /**
     * The actor that fired this bullet, used to determine the bullet's faction and prevent friendly fire.
     */
    private final Actor owner;

    /**
     * The amount of damage this bullet inflicts on hit.
     */
    private final double damage;

    /**
     * Constructs a bullet with specified size, position, and damage. <br>
     * This bullet has no owner and belongs to the NEUTRAL faction, meaning it can damage both FRIENDLY and ENEMY actors.
     *
     * @param size the size of the bullet
     * @param position the initial position of the bullet
     * @param damage the damage this bullet inflicts
     */
    public Bullet(Vector size, Vector position, double damage) {
        this(size, position, null, damage);
    }

    /**
     * Constructs a bullet with specified size, position, owner, and damage.
     *
     * @param size the size of the bullet
     * @param position the initial position of the bullet
     * @param owner the actor that fired this bullet
     * @param damage the damage this bullet inflicts
     */
    public Bullet(Vector size, Vector position, Actor owner, double damage) {
        super(size, position);
        this.owner = owner;
        this.damage = damage;
    }

    @Override
    public void act(GameContext context) {
        super.act(context);

        // Check for collisions with damageable objects
        for (DamageableGameObject actor : context.getDamageableGameObjects()) {
            // If the actor is from a different faction and intersects with the bullet, apply damage and remove the bullet
            if (getFaction().canDamage(actor.getFaction()) && intersects(actor)) {
                actor.damage(damage);
                context.removeActor(this);
                return;
            }
        }

        // Remove bullet if it goes off-screen
        if (getPosition().getY() < -getSize().getY() || getPosition().getY() > context.getHeight()) {
            context.removeActor(this);
        }
    }

    /**
     * Gets the faction of this bullet, which is determined by its owner.
     *
     * @return the owner's faction
     */
    @Override
    public Faction getFaction() {
        return owner.getFaction();
    }

    /**
     * Renders the bullet as a filled circle with the faction's color.
     *
     * @param g the Graphics object used for drawing
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(getFaction().getColor());
        g.fillOval((int) getPosition().getX(), (int) getPosition().getY(), (int) getSize().getX(), (int) getSize().getY());
    }
}
