public abstract class GameObject implements Actor {

    private Vector size;
    private Vector position;
    private Vector velocity;

    public GameObject(Vector size, Vector position) {
        this.size = size;
        this.position = position;
        this.velocity = Vector.ZERO;
    }

    @Override
    public void act(GameContext context) {
        setPosition(getPosition().add(getVelocity()));
    }

    public Vector getSize() {
        return size;
    }

    public void setSize(Vector size) {
        this.size = size;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }
}
