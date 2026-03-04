import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner {

    private final int level;
    private final Random random = new Random();
    private final List<EnemyType> availableTypes = new ArrayList<>();
    private final int spawnInterval;
    private long lastSpawnFrame = 0;
    private int enemiesSpawned = 0;
    private final int maxEnemies;

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

    private void spawnNextEnemy(GameContext context) {
        EnemyType type = chooseEnemyType();
        double x = random.nextDouble() * (context.getWidth() - 75);
        double y = random.nextDouble() * (context.getHeight() / 3.0);
        Vector position = new Vector(x, y);
        EnemyPlane enemyPlane = new EnemyPlane(position, type, level, random.nextBoolean());
        context.addActor(enemyPlane);
    }

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