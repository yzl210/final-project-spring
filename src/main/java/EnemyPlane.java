import javax.swing.*;
import java.awt.*;

/**
 * Represents an enemy plane with computer-controlled movement and shooting behavior. <br>
 * Enemies move back and forth, constrained to the upper half of the screen, <br>
 * and fire bullets downward at the player.
 */
public class EnemyPlane extends DamageableGameObject {

    /**
     * The sprite image for enemy planes.
     */
    private static final Image IMAGE = new ImageIcon(EnemyPlane.class.getResource("plane.png")).getImage();

    /**
     * The type of this enemy, determining its stats.
     */
    private final EnemyType type;

    /**
     * The level of this enemy, scaling its health, speed, damage, and fire rate.
     */
    private final int level;

    /**
     * The frame number when this enemy last fired a bullet.
     */
    private long lastFireFrame;

    /**
     * Whether the enemy is currently moving right (true) or left (false).
     */
    private boolean movingRight;

    /**
     * Whether the enemy is currently moving down (true) or up (false).
     */
    private boolean movingDown;

    /**
     * Constructs an enemy plane with the specified position, type, level, and initial direction. <br>
     * Health scales by 20% per level above 1. <br>
     * Vertical direction is randomized.
     *
     * @param position the initial position
     * @param type the enemy type determining base stats
     * @param level the difficulty level (1+)
     * @param movingRight true if initially moving right, false if moving left
     */
    public EnemyPlane(Vector position, EnemyType type, int level, boolean movingRight) {
        super(new Vector(75, 75), position, type.getHealth() * (1 + (level - 1) * 0.2));
        this.type = type;
        this.level = level;
        this.movingRight = movingRight;
        this.movingDown = Math.random() > 0.5;
        this.lastFireFrame = 0;
    }

    /**
     * Updates the enemy plane each game frame.
     */
    @Override
    public void act(GameContext context) {
        super.act(context);

        // If the enemy is dead, notify the context and skip further actions
        if (isDead()) {
            context.onEnemyKilled(this);
            return;
        }

        // Update movement based on current direction and speed, which scales with level
        Vector currentPos = getPosition();
        Vector size = getSize();

        double speed = type.getSpeed() * (1 + (level - 1) * 0.1);
        setVelocity(new Vector(
            (movingRight ? 1 : -1) * speed,
            (movingDown ? 1 : -1) * speed * 0.5
        ));

        // Bounce horizontally at screen edges
        if (currentPos.getX() <= 0) {
            movingRight = true;
        } else if (currentPos.getX() >= context.getWidth() - size.getX()) {
            movingRight = false;
        }

        // Bounce vertically within upper half of the screen
        double maxY = context.getHeight() / 2.0;
        if (currentPos.getY() <= 0) {
            movingDown = true;
        } else if (currentPos.getY() >= maxY - size.getY()) {
            movingDown = false;
        }

        // Handle shooting: fire a bullet if enough frames have passed since last shot, with level-scaled fire rate and damage
        if (context.getFrameCount() - lastFireFrame >= Math.max(15, (int) (type.getFireDelay() * (1 - (level - 1) * 0.05)))) {
            lastFireFrame = context.getFrameCount();
            Vector bulletPos = new Vector(
                getPosition().getX() + getSize().getX() / 2 - 5,
                getPosition().getY() + getSize().getY()
            );
            Bullet bullet = new Bullet(new Vector(10, 10), bulletPos, this, type.getDamage() * (1 + (level - 1) * 0.15));
            bullet.setVelocity(new Vector(0, 3));
            context.addActor(bullet);
        }
    }

    @Override
    public Faction getFaction() {
        return Faction.ENEMY;
    }

    public EnemyType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void paint(Graphics g) {
        // Tint the enemy plane sprite with the color associated with its type
        g.setColor(type.getTint());
        // Draw the sprite
        g.drawImage(IMAGE, (int) getPosition().getX(), (int) (getPosition().getY() + getSize().getY()), (int) getSize().getX(), -(int) getSize().getY(), null);
        // Draw the health bar below the plane using the helper method
        drawHealthBar(g, (int) getPosition().getX(), (int) (getPosition().getY() + getSize().getY() + 5), (int) getSize().getX(), 5);
    }
}