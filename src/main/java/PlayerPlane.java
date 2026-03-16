import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the player-controlled plane in the game. <br>
 * Move with WASD keys, shoot with space, and manages upgrades.
 */
public class PlayerPlane extends DamageableGameObject {

    /**
     * The sprite image for the player plane.
     */
    private static final Image IMAGE = new ImageIcon(PlayerPlane.class.getResource("plane.png")).getImage();

    /**
     * The base fire delay in frames between shots (before upgrades).
     */
    private static final int BASE_FIRE_DELAY = 30;

    /**
     * The frame number when the player last fired a bullet.
     */
    private long lastFireFrame = -BASE_FIRE_DELAY;

    /**
     * Map storing upgrade levels for each upgrade type. <br>
     */
    private final Map<Upgrade, Integer> upgradeLevels = new HashMap<>();

    /**
     * Constructs a player plane with default size, position, and health. <br>
     * Initializes all upgrades to level 0.
     */
    public PlayerPlane() {
        super(new Vector(75, 75), new Vector(100, 100), 100);
        for (Upgrade upgrade : Upgrade.values()) {
            upgradeLevels.put(upgrade, 0);
        }
    }

    /**
     * Applies an upgrade by incrementing its level. <br>
     * For MAX_HEALTH upgrades, immediately increases both current and maximum health by 20.
     *
     * @param upgrade the upgrade to apply
     */
    public void applyUpgrade(Upgrade upgrade) {
        upgradeLevels.put(upgrade, upgradeLevels.get(upgrade) + 1);
        if (upgrade == Upgrade.MAX_HEALTH) {
            setMaxHealth(getMaxHealth() + 20);
            setHealth(getHealth() + 20);
        }
    }

    public int getUpgradeLevel(Upgrade upgrade) {
        return upgradeLevels.get(upgrade);
    }

    /**
     * Calculates the current movement speed based on upgrades. <br>
     * Base speed is 5, increased by 1 per SPEED upgrade level.
     *
     * @return the current speed value
     */
    private double getCurrentSpeed() {
        return 5 + upgradeLevels.get(Upgrade.SPEED);
    }

    /**
     * Calculates the current bullet damage based on upgrades. <br>
     * Base damage is 1, increased by 2 per DAMAGE upgrade level.
     *
     * @return the current damage value
     */
    private double getCurrentDamage() {
        return 1 + (upgradeLevels.get(Upgrade.DAMAGE) * 2);
    }

    /**
     * Calculates the current fire delay in frames based on upgrades. <br>
     * Delay scales by 0.9^level
     *
     * @return the current fire delay in frames
     */
    private int getCurrentFireDelay() {
        return (int) (BASE_FIRE_DELAY * Math.pow(0.9, upgradeLevels.get(Upgrade.FIRE_RATE)));
    }

    /**
     * Updates the player plane each game frame. <br>
     */
    @Override
    public void act(GameContext context) {
        super.act(context);
        // Handle movement input
        int dx = 0, dy = 0;
        if (context.isKeyPressed(KeyEvent.VK_W)) dy -= 1;
        if (context.isKeyPressed(KeyEvent.VK_S)) dy += 1;
        if (context.isKeyPressed(KeyEvent.VK_A)) dx -= 1;
        if (context.isKeyPressed(KeyEvent.VK_D)) dx += 1;
        if (dx == 0 && dy == 0) {
            setVelocity(Vector.ZERO);
        } else {
            double len = Math.hypot(dx, dy);
            setVelocity(new Vector(dx / len * getCurrentSpeed(), dy / len * getCurrentSpeed()));
        }

        // Constrain position within screen bounds
        Vector currentPos = getPosition();
        Vector size = getSize();
        double newX = Math.max(0, Math.min(context.getWidth() - size.getX(), currentPos.getX()));
        double newY = Math.max(0, Math.min(context.getHeight() - size.getY(), currentPos.getY()));
        setPosition(new Vector(newX, newY));


        // Handle shooting
        if (context.isKeyPressed(KeyEvent.VK_SPACE) && context.getFrameCount() - lastFireFrame >= getCurrentFireDelay()) {
            lastFireFrame = context.getFrameCount();
            Vector bulletPos = new Vector(
                    getPosition().getX() + getSize().getX() / 2 - 5,
                    getPosition().getY()
            );
            Bullet bullet = new Bullet(new Vector(10, 10), bulletPos, this, getCurrentDamage());
            bullet.setVelocity(new Vector(0, -5));
            context.addActor(bullet);
        }
    }

    @Override
    public Faction getFaction() {
        return Faction.FRIENDLY;
    }

    @Override
    public void paint(Graphics g) {
        // Draw the plane sprite
        g.drawImage(IMAGE, (int) getPosition().getX(), (int) getPosition().getY(), (int) getSize().getX(), (int) getSize().getY(), null);
        // Draw the health bar below the plane using the helper method
        drawHealthBar(g, (int) getPosition().getX(), (int) (getPosition().getY() + getSize().getY() + 5), (int) getSize().getX(), 5);
    }
}
