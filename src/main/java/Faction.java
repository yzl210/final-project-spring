import java.awt.*;

/**
 * Represents the different factions that actors can belong to in the game. <br>
 * Factions determine visual representation and combat interactions of actors.
 */
public enum Faction {
    /**
     * Friendly faction, represented by blue color. <br>
     * Used for player plane and their bullets.
     */
    FRIENDLY(Color.BLUE),

    /**
     * Enemy faction, represented by red color. <br>
     * Used for enemy planes and their bullets.
     */
    ENEMY(Color.RED),

    /**
     * Neutral faction, represented by gray color. <br>
     * Used for actors that don't belong to any side.
     */
    NEUTRAL(Color.GRAY);

    /**
     * The color associated with this faction for visual representation (e.g., HP bars, bullet color).
     */
    private final Color color;

    Faction(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Determines if this faction can damage another faction. <br>
     * Actors can damage actors from different factions but not their own.
     *
     * @param other the other faction to check against
     * @return true if this faction can damage the other faction, false otherwise
     */
    public boolean canDamage(Faction other) {
        return this != other;
    }
}
