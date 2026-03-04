import javax.swing.*;

// Based on code from here: https://stackoverflow.com/questions/65907092/where-should-i-put-the-game-loop-in-the-swing-app
public class GameManager {

    public static final boolean DEBUG = false;

    private final GameBoard gameBoard;
    // how many frames should be drawn in a second
    private final int FRAMES_PER_SECOND = 60;
    // calculate how many nanoseconds each frame should take for our target frames per second.
    private final long TIME_BETWEEN_UPDATES = 1000000000 / FRAMES_PER_SECOND;
    public GameManager() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameBoard = new GameBoard();
        window.add(gameBoard);
        window.addKeyListener(gameBoard);
        window.pack();
        window.setVisible(true);

        // Run the game loop on its own Thread, so that it doesn't interfere with Swing updates.
        // track number of frames
        // if you're worried about visual hitches more than perfect timing, set this to 1. else 5 should be okay
        // we will need the last update time.
        // store the time we started this will be used for updating map and character animations
        // do as many game updates as we need to, potentially playing catchup.
        // if for some reason an update takes forever, we don't want to do an insane number of catchups.
        // if you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
        // Trigger a re-render of the game board
        //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
        Thread gameLoop = new Thread(() -> {
            // if you're worried about visual hitches more than perfect timing, set this to 1. else 5 should be okay
            final int MAX_UPDATES_BETWEEN_RENDER = 1;

            // we will need the last update time.
            long lastUpdateTime = System.nanoTime();
            // store the time we started this will be used for updating map and character animations
            long currTime = System.currentTimeMillis();

            while (true) {
                long now = System.nanoTime();
                long elapsedTime = System.currentTimeMillis() - currTime;
                currTime += elapsedTime;

                int updateCount = 0;
                // do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime >= TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BETWEEN_RENDER) {
                    gameBoard.act();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                // if for some reason an update takes forever, we don't want to do an insane number of catchups.
                // if you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime >= TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                // Trigger a re-render of the game board
                gameBoard.repaint();

                long lastRenderTime = now;

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TIME_BETWEEN_UPDATES && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();
                    now = System.nanoTime();
                }
            }
        });
        gameLoop.start();
    }

    public static void main(String[] args) {
        new GameManager();
    }
}
