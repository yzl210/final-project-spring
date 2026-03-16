import java.util.List;

/**
 * Provides access to game state and utilities for actors during gameplay. <br>
 * Allows actors to query input, manage other actors, and access game information.
 */
public interface GameContext {
    long getFrameCount();

    boolean isKeyPressed(int keyCode);

    void addActor(Actor actor);

    void removeActor(Actor actor);

    int getWidth();

    int getHeight();

    List<Actor> getActors();

    List<DamageableGameObject> getDamageableGameObjects();

    PlayerPlane getPlayerPlane();

    int getCurrentLevel();

    /**
     * Notifies the game context that an enemy has been killed. <br>
     * Used for tracking score, spawning rewards, or progression logic.
     *
     * @param enemy the enemy plane that was killed
     */
    void onEnemyKilled(EnemyPlane enemy);
}
