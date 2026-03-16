import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the spawning of enemy planes for a level. <br>
 * Controls spawn rate, enemy type distribution, and total enemy count based on level difficulty.
 */
public class EnemySpawner {

    /**
     * The difficulty level determining spawn parameters.
     */
    private final int level;

    /**
     * Random number generator for spawn positioning and type selection.
     */
    private final Random random = new Random();

    /**
     * Weighted pool of enemy types available at this level. <br>
     * Types appear multiple times to control spawn probability.
     */
    private final List<EnemyType> availableTypes = new ArrayList<>();

    /**
     * The number of frames between enemy spawns.
     */
    private final int spawnInterval;

    /**
     * The frame number when the last enemy was spawned.
     */
    private long lastSpawnFrame = 0;

    /**
     * The number of enemies spawned so far this level.
     */
    private int enemiesSpawned = 0;

    /**
     * The maximum number of enemies to spawn for this level.
     */
    private final int maxEnemies;

    /**
     * Constructs an enemy spawner for the specified level. <br>
     * Spawn interval decreases with level (min 30 frames). <br>
     * Max enemies increases with level (5 + level * 2). <br>
     * Enemy types unlock progressively: SCOUT (lvl 1+), FIGHTER (lvl 2+), HEAVY (lvl 3+), SNIPER (lvl 4+).
     *
     * @param level the difficulty level (1+)
     */
    public EnemySpawner(int level) {
        this.level = level;
        this.spawnInterval = Math.max(30, 120 - (level * 10));
        this.maxEnemies = 5 + level * 2;

        if (level >= 1) {
            for (int i = 0; i < 50; i++) availableTypes.add(EnemyType.SCOUT);
        }
        if (level >= 2) {
            for (int i = 0; i < 30; i++) availableTypes.add(EnemyType.FIGHTER);
        }
        if (level >= 3) {
            for (int i = 0; i < 15; i++) availableTypes.add(EnemyType.HEAVY);
        }
        if (level >= 4) {
            for (int i = 0; i < 15; i++) availableTypes.add(EnemyType.SNIPER);
        }
    }

    /**
     * Updates the spawner each game tick. <br>
     * Spawns a new enemy if the spawn interval has elapsed and max enemies not reached.
     *
     * @param context the game context for spawning enemies
     */
    public void update(GameContext context) {
        if (enemiesSpawned >= maxEnemies) {
            return;
        }

        long currentFrame = context.getFrameCount();
        if (currentFrame - lastSpawnFrame >= spawnInterval) {
            spawnNextEnemy(context);
            lastSpawnFrame = currentFrame;
            enemiesSpawned++;
        }
    }

    /**
     * Spawns a new enemy at a random position in the upper third of the screen. <br>
     * Enemy type and initial direction are randomized.
     *
     * @param context the game context for adding the enemy
     */
    private void spawnNextEnemy(GameContext context) {
        EnemyType type = chooseEnemyType();
        double x = random.nextDouble() * (context.getWidth() - 75);
        double y = random.nextDouble() * (context.getHeight() / 3.0);
        Vector position = new Vector(x, y);
        EnemyPlane enemyPlane = new EnemyPlane(position, type, level, random.nextBoolean());
        context.addActor(enemyPlane);
    }

    /**
     * Randomly selects an enemy type from the weighted pool. <br>
     * More common types appear more frequently due to multiple entries in the pool.
     *
     * @return a randomly chosen enemy type
     */
    private EnemyType chooseEnemyType() {
        return availableTypes.get(random.nextInt(availableTypes.size()));
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public int getMaxEnemies() {
        return maxEnemies;
    }
}