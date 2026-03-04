public enum Upgrade {
    DAMAGE("Damage +2", 5, 10),
    FIRE_RATE("Fire Rate +10%", 5, 10),
    SPEED("Speed +1", 10, 10),
    MAX_HEALTH("Max Health +20", 10, 10);

    private final String name;
    private final int cost;
    private final int maxLevel;

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

    public int getCostAtLevel(int level) {
        return (int) (cost * Math.pow(1.5, level));
    }
}