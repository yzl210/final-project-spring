import java.awt.*;

public abstract class DamageableGameObject extends GameObject {
    private double health;
    private double maxHealth;

    public DamageableGameObject(Vector size, Vector position, double maxHealth) {
        super(size, position);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public void act(GameContext context) {
        super.act(context);
        if (isDead()) {
            context.removeActor(this);
        }
    }

    public void damage(double damage) {
        health = Math.max(0, health - damage);
    }

    protected double setHealth(double health) {
        this.health = health;
        return this.health;
    }

    public double getHealth() {
        return health;
    }

    protected double setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        return this.maxHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public boolean isDead() {
        return health <= 0;
    }

    protected void drawHealthBar(Graphics g, int x, int y, int width, int height) {
        g.setColor(getFaction().getColor());
        g.fillRect(x, y, (int) (width * (health / maxHealth)), height);
    }
}