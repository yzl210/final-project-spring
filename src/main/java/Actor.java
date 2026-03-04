import java.awt.*;

public interface Actor {
    void act(GameContext context);

    void paint(Graphics g);

    Vector getPosition();

    Vector getSize();

    default Faction getFaction() {
        return Faction.NEUTRAL;
    }

    default boolean intersects(Actor other) {
        return getPosition().x() < other.getPosition().x() + other.getSize().x() &&
                getPosition().x() + getSize().x() > other.getPosition().x() &&
                getPosition().y() < other.getPosition().y() + other.getSize().y() &&
                getPosition().y() + getSize().y() > other.getPosition().y();
    }
}
