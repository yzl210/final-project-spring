import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.Queue;

/**
 * The main game panel that manages game state, rendering, and input. <br>
 * Handles the game loop, level progression, upgrade shop, and UI rendering.
 */
public class GameBoard extends JPanel implements KeyListener {
    /**
     * The game context provided to actors.
     */
    private final GameContext context = new GameBoardContext();

    /**
     * The total number of frames elapsed since game start.
     */
    private long frameCount = 0;

    /**
     * The player's plane.
     */
    private PlayerPlane playerPlane = new PlayerPlane();

    /**
     * All active actors in the game.
     */
    private final List<Actor> actors = new ArrayList<>();

    /**
     * All active damageable game objects for collision detection.
     */
    private final List<DamageableGameObject> damageableGameObjects = new ArrayList<>();

    /**
     * Set of currently pressed key codes.
     */
    private final Set<Integer> pressedKeys = new HashSet<>();

    /**
     * Queue of tasks to execute before the next frame (e.g. actor add/remove).
     */
    private final Queue<Runnable> pendingTasks = new ArrayDeque<>();

    /**
     * The current level.
     */
    private int currentLevel = 1;

    /**
     * The enemy spawner for the current level.
     */
    private EnemySpawner enemySpawner = new EnemySpawner(currentLevel);

    /**
     * The player's total score.
     */
    private int score = 0;

    /**
     * The player's currency for purchasing upgrades.
     */
    private int coins = 0;

    /**
     * Whether the current level has ended (upgrade shop active).
     */
    private boolean levelEnd = false;

    /**
     * Whether the game is over (player died).
     */
    private boolean gameOver = false;

    /**
     * Constructs the game board with 600x600 dimensions and adds the player plane.
     */
    public GameBoard() {
        super();
        this.setPreferredSize(new Dimension(600, 600));
        addActor(playerPlane);
    }

    /**
     * Adds an actor to the game world. <br>
     *
     * @param actor the actor to add
     */
    private void addActor(Actor actor) {
        this.actors.add(actor);
        if (actor instanceof DamageableGameObject damageable)
            this.damageableGameObjects.add(damageable);
    }

    /**
     * Removes an actor from the game world. <br>
     *
     * @param actor the actor to remove
     */
    private void removeActor(Actor actor) {
        this.actors.remove(actor);
        if (actor instanceof DamageableGameObject damageable)
            this.damageableGameObjects.remove(damageable);
    }

    /**
     * Main game loop called every frame by the GameManager. <br>
     */
    public void act() {
        // If the game is over, skip all updates and wait for restart
        if (gameOver) {
            return;
        }
        frameCount++;

        // Execute any pending tasks
        while (!pendingTasks.isEmpty()) {
            pendingTasks.poll().run();
        }

        // Update all actors
        for (Actor actor : new ArrayList<>(actors)) {
            actor.act(context);
        }

        // Check for game over condition
        if (playerPlane.isDead()) {
            gameOver = true;
            return;
        }

        // If the level hasn't ended, update the enemy spawner
        if (!levelEnd) {
            enemySpawner.update(context);
        }
    }

    /**
     * Ends the current level by removing all enemies and bullets. <br>
     * Opens the upgrade shop UI.
     */
    private void endLevel() {
        for (Actor actor : new ArrayList<>(actors)) {
            if (actor instanceof EnemyPlane || actor instanceof Bullet) {
                removeActor(actor);
            }
        }
        levelEnd = true;
    }

    /**
     * Advances to the next level. <br>
     * Increments level counter, creates new enemy spawner, and restores player health to maximum.
     */
    private void nextLevel() {
        levelEnd = false;
        currentLevel++;
        enemySpawner = new EnemySpawner(currentLevel);
        playerPlane.setHealth(playerPlane.getMaxHealth());
    }

    /**
     * Attempts to purchase an upgrade if the player has enough coins and hasn't reached max level. <br>
     * Deducts cost and applies upgrade on success.
     *
     * @param upgrade the upgrade to purchase
     */
    private void tryPurchaseUpgrade(Upgrade upgrade) {
        int currentLevel = playerPlane.getUpgradeLevel(upgrade);
        if (currentLevel >= upgrade.getMaxLevel()) {
            return;
        }

        int cost = upgrade.getCostAtLevel(currentLevel);
        if (coins >= cost) {
            coins -= cost;
            playerPlane.applyUpgrade(upgrade);
        }
    }

    /**
     * Renders all actors and UI elements. <br>
     * In debug mode, also draws bounding boxes and actor names.
     *
     * @param g the Graphics object used for drawing
     */
    @Override
    public void paintComponent(Graphics g) {
        List<Actor> snapshot = new ArrayList<>(actors);
        for (Actor actor : snapshot) {
            actor.paint(g);
            if (GameManager.DEBUG) {
                Vector pos = actor.getPosition();
                Vector size = actor.getSize();
                g.setColor(actor.getFaction().getColor());
                g.drawRect((int) pos.getX(), (int) pos.getY(), (int) size.getX(), (int) size.getY());
                g.drawString(actor.getClass().getSimpleName(), (int) pos.getX(), (int) pos.getY() - 5);
            }
        }

        drawUI(g);
    }

    /**
     * Draws the main game UI including level, score, coins, and progress bar. <br>
     * Also renders game over or level end UI when applicable.
     *
     * @param g the Graphics object used for drawing
     */
    private void drawUI(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        String levelText = "Level: " + currentLevel;
        g.drawString(levelText, 10, 25);
        String scoreText = "Score: " + score;
        g.drawString(scoreText, 150, 25);
        String coinsText = "Coins: " + coins;
        g.drawString(coinsText, 300, 25);


        int progressBarWidth = 200;
        int progressBarHeight = 10;
        int progress = enemySpawner.getEnemiesSpawned() * 100 / enemySpawner.getMaxEnemies();
        g.setColor(Color.GRAY);
        g.fillRect(10, 50, progressBarWidth, progressBarHeight);
        g.setColor(Color.GREEN);
        g.fillRect(10, 50, (progressBarWidth * progress) / 100, progressBarHeight);

        if (gameOver) {
            drawGameOverUI(g);
        } else if (levelEnd) {
            drawLevelEndUI(g);
        }
    }

    /**
     * Draws the game over screen with final score, level reached, and restart instruction.
     *
     * @param g the Graphics object used for drawing
     */
    private void drawGameOverUI(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverText);
        g.drawString(gameOverText, (getWidth() - textWidth) / 2, getHeight() / 2 - 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String finalScoreText = "Final Score: " + score;
        textWidth = g.getFontMetrics().stringWidth(finalScoreText);
        g.drawString(finalScoreText, (getWidth() - textWidth) / 2, getHeight() / 2);

        String finalLevelText = "Reached Level: " + currentLevel;
        textWidth = g.getFontMetrics().stringWidth(finalLevelText);
        g.drawString(finalLevelText, (getWidth() - textWidth) / 2, getHeight() / 2 + 40);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String restartText = "Press R to Restart";
        textWidth = g.getFontMetrics().stringWidth(restartText);
        g.drawString(restartText, (getWidth() - textWidth) / 2, getHeight() / 2 + 100);
    }

    /**
     * Draws the level end screen with upgrade shop UI. <br>
     * Shows available upgrades with costs, levels, and purchase options (keys 1-4). <br>
     * Displays instruction to press ENTER to continue to next level.
     *
     * @param g the Graphics object used for drawing
     */
    private void drawLevelEndUI(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "LEVEL " + currentLevel + " COMPLETE!";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        g.drawString(title, (getWidth() - textWidth) / 2, 80);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        String statsText = "Score: " + score + " | Coins: " + coins;
        textWidth = g.getFontMetrics().stringWidth(statsText);
        g.drawString(statsText, (getWidth() - textWidth) / 2, 120);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String shopTitle = "UPGRADE SHOP";
        textWidth = g.getFontMetrics().stringWidth(shopTitle);
        g.drawString(shopTitle, (getWidth() - textWidth) / 2, 160);

        Upgrade[] upgrades = Upgrade.values();
        int boxWidth = 350;
        int boxHeight = 80;
        int startX = (getWidth() - boxWidth) / 2;
        int startY = 190;
        int spacing = 10;

        for (int i = 0; i < upgrades.length; i++) {
            Upgrade upgrade = upgrades[i];
            int level = playerPlane.getUpgradeLevel(upgrade);
            int cost = upgrade.getCostAtLevel(level);
            boolean maxed = level >= upgrade.getMaxLevel();
            boolean canAfford = coins >= cost;

            int boxY = startY + i * (boxHeight + spacing);

            if (maxed) {
                g.setColor(new Color(100, 100, 100, 200));
            } else if (canAfford) {
                g.setColor(new Color(0, 150, 0, 200));
            } else {
                g.setColor(new Color(150, 0, 0, 200));
            }
            g.fillRoundRect(startX, boxY, boxWidth, boxHeight, 15, 15);

            if (maxed) {
                g.setColor(Color.GRAY);
            } else if (canAfford) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }
            g.drawRoundRect(startX, boxY, boxWidth, boxHeight, 15, 15);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("[" + (i + 1) + "]", startX + 15, boxY + 40);

            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(upgrade.getName(), startX + 60, boxY + 30);

            int barX = startX + 60;
            int barY = boxY + 45;
            int barWidth = 150;
            int barHeight = 15;
            g.setColor(new Color(50, 50, 50));
            g.fillRect(barX, barY, barWidth, barHeight);
            g.setColor(Color.YELLOW);
            int progressWidth = (barWidth * level) / upgrade.getMaxLevel();
            g.fillRect(barX, barY, progressWidth, barHeight);
            g.setColor(Color.WHITE);
            g.drawRect(barX, barY, barWidth, barHeight);

            g.setFont(new Font("Arial", Font.PLAIN, 12));
            String levelText = level + "/" + upgrade.getMaxLevel();
            g.drawString(levelText, barX + barWidth + 10, barY + 12);

            g.setFont(new Font("Arial", Font.BOLD, 16));
            if (maxed) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawString("MAX", startX + 280, boxY + 35);
            } else {
                g.setColor(Color.YELLOW);
                g.drawString("Cost: " + cost, startX + 250, boxY + 35);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String instructions = "Press ENTER to continue to next level";
        textWidth = g.getFontMetrics().stringWidth(instructions);
        g.drawString(instructions, (getWidth() - textWidth) / 2, getHeight() - 40);
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Add the pressed key to the set of currently pressed keys
        this.pressedKeys.add(e.getKeyCode());

        if (gameOver) {
            // If the game is over, only listen for the restart key
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }
        } else if (levelEnd) {
            // If the level has ended, listen for upgrade purchases and advancing to next level
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                nextLevel();
            }

            Upgrade[] upgrades = Upgrade.values();
            for (int i = 0; i < upgrades.length; i++) {
                if (e.getKeyCode() == KeyEvent.VK_1 + i) {
                    tryPurchaseUpgrade(upgrades[i]);
                }
            }
        }
    }

    /**
     * Resets the game state to start a new game. <br>
     * Clears all actors, resets score and coins, and reinitializes player and enemy spawner.
     */
    private void restartGame() {
        gameOver = false;
        levelEnd = false;
        currentLevel = 1;
        score = 0;
        coins = 0;
        frameCount = 0;

        actors.clear();
        damageableGameObjects.clear();
        pendingTasks.clear();

        playerPlane = new PlayerPlane();
        addActor(playerPlane);

        enemySpawner = new EnemySpawner(currentLevel);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.pressedKeys.remove(e.getKeyCode());
    }

    private class GameBoardContext implements GameContext {
        @Override
        public long getFrameCount() {
            return frameCount;
        }

        @Override
        public boolean isKeyPressed(int keyCode) {
            return pressedKeys.contains(keyCode);
        }

        @Override
        public void addActor(Actor actor) {
            pendingTasks.offer(() -> GameBoard.this.addActor(actor));
        }

        @Override
        public void removeActor(Actor actor) {
            pendingTasks.offer(() -> GameBoard.this.removeActor(actor));
        }

        @Override
        public int getWidth() {
            return GameBoard.this.getWidth();
        }

        @Override
        public int getHeight() {
            return GameBoard.this.getHeight();
        }

        @Override
        public List<Actor> getActors() {
            return Collections.unmodifiableList(actors);
        }

        @Override
        public List<DamageableGameObject> getDamageableGameObjects() {
            return Collections.unmodifiableList(damageableGameObjects);
        }

        @Override
        public PlayerPlane getPlayerPlane() {
            return playerPlane;
        }

        @Override
        public int getCurrentLevel() {
            return currentLevel;
        }

        @Override
        public void onEnemyKilled(EnemyPlane enemy) {
            EnemyType type = enemy.getType();
            int points = enemy.getLevel() * type.getPoints();
            score += points;
            coins += points;

            // Check if all enemies have been spawned and killed to end the level
            long enemyCount = actors.stream()
                    .filter(actor -> actor instanceof EnemyPlane plane && !plane.isDead())
                    .count();

            if (enemySpawner.getEnemiesSpawned() >= enemySpawner.getMaxEnemies() && enemyCount == 0) {
                endLevel();
            }
        }
    }
}
