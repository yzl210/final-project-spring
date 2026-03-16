import java.awt.*;

/**
 * Represents an actor in the game that can act and rendered <br>
 */
public interface Actor {
    /**
     * Performs the actor's behavior for the current game tick.
     *
     * @param context the game context containing game state and utils
     */
    void act(GameContext context);

    /**
     * Renders the actor on the screen.
     *
     * @param g the Graphics object used for drawing
     */
    void paint(Graphics g);

    /**
     * Gets the current position of the actor.
     *
     * @return a Vector representing the actor's position
     */
    Vector getPosition();

    /**
     * Gets the size of the actor's bounding box.
     *
     * @return a Vector representing the actor's width and height
     */
    Vector getSize();

    /**
     * Gets the faction this actor belongs to. <br>
     * See {@link Faction} for more details. <br>
     * Default implementation returns NEUTRAL faction.
     *
     * @return the actor's faction
     */
    default Faction getFaction() {
        return Faction.NEUTRAL;
    }

    /**
     * Checks if this actor intersects with another actor
     *
     * @param other the other actor to check collision with
     * @return true if the actors overlap, false otherwise
     */
    default boolean intersects(Actor other) {
        return getPosition().getX() < other.getPosition().getX() + other.getSize().getX() &&
                getPosition().getX() + getSize().getX() > other.getPosition().getX() &&
                getPosition().getY() < other.getPosition().getY() + other.getSize().getY() &&
                getPosition().getY() + getSize().getY() > other.getPosition().getY();
    }
}
