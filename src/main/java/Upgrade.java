/**
 * Represents the different types of upgrades available to the player. <br>
 * Each upgrade has a name, base cost, and maximum level.
 */
public enum Upgrade {
    /**
     * Increases bullet damage by 2 per level.
     */
    DAMAGE("Damage +2", 5, 10),

    /**
     * Increases fire rate by 10% per level.
     */
    FIRE_RATE("Fire Rate +10%", 5, 10),

    /**
     * Increases movement speed by 1 per level.
     */
    SPEED("Speed +1", 10, 10),

    /**
     * Increases maximum health by 20 per level.
     */
    MAX_HEALTH("Max Health +20", 10, 10);

    /**
     * The display name of this upgrade.
     */
    private final String name;

    /**
     * The base cost of this upgrade at level 0.
     */
    private final int cost;

    /**
     * The maximum level this upgrade can reach.
     */
    private final int maxLevel;

    /**
     * Constructs an upgrade with the specified properties.
     *
     * @param name the display name
     * @param cost the base cost
     * @param maxLevel the maximum level
     */
    Upgrade(String name, int cost, int maxLevel) {
        this.name = name;
        this.cost = cost;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public int getBaseCost() {
        return cost;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Calculates the cost of this upgrade at a specific level. <br>
     * Cost scales exponentially: base cost * 1.5^level.
     *
     * @param level the level to calculate cost for
     * @return the cost at the specified level
     */
    public int getCostAtLevel(int level) {
        return (int) (cost * Math.pow(1.5, level));
    }
}