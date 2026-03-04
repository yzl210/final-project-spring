import java.util.List;

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

    void onEnemyKilled(EnemyPlane enemy);
}
