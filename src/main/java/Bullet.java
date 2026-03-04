import java.awt.*;

public class Bullet extends GameObject {
    private final Actor owner;
    private final double damage;

    public Bullet(Vector size, Vector position, double damage) {
        this(size, position, null, damage);
    }

    public Bullet(Vector size, Vector position, Actor owner, double damage) {
        super(size, position);
        this.owner = owner;
        this.damage = damage;
    }

    public Bullet(Vector position, Actor owner, double damage) {
        super(new Vector(5 + damage * 1.5, 5 + damage * 1.5), position);
        this.owner = owner;
        this.damage = damage;
    }

    @Override
    public void act(GameContext context) {
        super.act(context);

        // Check for collisions with damageable objects
        for (DamageableGameObject actor : context.getDamageableGameObjects()) {
            if (getFaction().canDamage(actor.getFaction()) && intersects(actor)) {
                actor.damage(damage);
                context.removeActor(this);
                return;
            }
        }

        // Remove bullet if it goes off-screen
        if (getPosition().y() < -getSize().y() || getPosition().y() > context.getHeight()) {
            context.removeActor(this);
        }
    }

    @Override
    public Faction getFaction() {
        return owner.getFaction();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getFaction().getColor());
        g.fillOval((int) getPosition().x(), (int) getPosition().y(), (int) getSize().x(), (int) getSize().y());
    }
}
