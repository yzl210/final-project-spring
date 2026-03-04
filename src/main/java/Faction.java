import java.awt.*;

public enum Faction {
    FRIENDLY(Color.BLUE),
    ENEMY(Color.RED),
    NEUTRAL(Color.GRAY);

    private final Color color;

    Faction(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean canDamage(Faction other) {
        return this != other;
    }
}
