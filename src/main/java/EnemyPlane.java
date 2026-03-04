import javax.swing.*;
import java.awt.*;

public class EnemyPlane extends DamageableGameObject {

    private static final Image IMAGE = new ImageIcon(EnemyPlane.class.getResource("plane.png")).getImage();
    private final EnemyType type;
    private final int level;
    private long lastFireFrame;
    private boolean movingRight;
    private boolean movingDown;

    public EnemyPlane(Vector position, EnemyType type, int level, boolean movingRight) {
        super(new Vector(75, 75), position, type.getHealth() * (1 + (level - 1) * 0.2));
        this.type = type;
        this.level = level;
        this.movingRight = movingRight;
        this.movingDown = Math.random() > 0.5;
        this.lastFireFrame = 0;
    }


    @Override
    public void act(GameContext context) {
        super.act(context);
        
        if (isDead()) {
            context.onEnemyKilled(this);
            return;
        }

        Vector currentPos = getPosition();
        Vector size = getSize();

        double speed = type.getSpeed() * (1 + (level - 1) * 0.1);
        setVelocity(new Vector(
            (movingRight ? 1 : -1) * speed,
            (movingDown ? 1 : -1) * speed * 0.5
        ));

        if (currentPos.x() <= 0) {
            movingRight = true;
        } else if (currentPos.x() >= context.getWidth() - size.x()) {
            movingRight = false;
        }

        double maxY = context.getHeight() / 2.0;
        if (currentPos.y() <= 0) {
            movingDown = true;
        } else if (currentPos.y() >= maxY - size.y()) {
            movingDown = false;
        }

        if (context.getFrameCount() - lastFireFrame >= Math.max(15, (int) (type.getFireDelay() * (1 - (level - 1) * 0.05)))) {
            lastFireFrame = context.getFrameCount();
            Vector bulletPos = new Vector(
                getPosition().x() + getSize().x() / 2 - 5,
                getPosition().y() + getSize().y()
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
        g.setColor(type.getTint());
        g.drawImage(IMAGE, (int) getPosition().x(), (int) (getPosition().y() + getSize().y()), (int) getSize().x(), -(int) getSize().y(), null);
        drawHealthBar(g, (int) getPosition().x(), (int) (getPosition().y() + getSize().y() + 5), (int) getSize().x(), 5);
    }
}