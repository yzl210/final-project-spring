import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class PlayerPlane extends DamageableGameObject {

    private static final Image IMAGE = new ImageIcon(PlayerPlane.class.getResource("plane.png")).getImage();

    private static final int BASE_FIRE_DELAY = 30;
    private long lastFireFrame = -BASE_FIRE_DELAY;
    private final Map<Upgrade, Integer> upgradeLevels = new HashMap<>();

    public PlayerPlane() {
        super(new Vector(75, 75), new Vector(100, 100), 100);
        for (Upgrade upgrade : Upgrade.values()) {
            upgradeLevels.put(upgrade, 0);
        }
    }

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

    private double getCurrentSpeed() {
        return 5 + upgradeLevels.get(Upgrade.SPEED);
    }

    private double getCurrentDamage() {
        return 1 + (upgradeLevels.get(Upgrade.DAMAGE) * 2);
    }

    private int getCurrentFireDelay() {
        return (int) (BASE_FIRE_DELAY * Math.pow(0.9, upgradeLevels.get(Upgrade.FIRE_RATE)));
    }

    @Override
    public void act(GameContext context) {
        super.act(context);
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

        Vector currentPos = getPosition();
        Vector size = getSize();
        double newX = Math.max(0, Math.min(context.getWidth() - size.x(), currentPos.x()));
        double newY = Math.max(0, Math.min(context.getHeight() - size.y(), currentPos.y()));
        setPosition(new Vector(newX, newY));


        if (context.isKeyPressed(KeyEvent.VK_SPACE) && context.getFrameCount() - lastFireFrame >= getCurrentFireDelay()) {
            lastFireFrame = context.getFrameCount();
            Vector bulletPos = new Vector(
                    getPosition().x() + getSize().x() / 2 - 5,
                    getPosition().y()
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
        g.drawImage(IMAGE, (int) getPosition().x(), (int) getPosition().y(), (int) getSize().x(), (int) getSize().y(), null);
        drawHealthBar(g, (int) getPosition().x(), (int) (getPosition().y() + getSize().y() + 5), (int) getSize().x(), 5);
    }
}
