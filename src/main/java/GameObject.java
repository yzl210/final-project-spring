/**
 * Abstract base class for all game objects that have position, size, and velocity. <br>
 * Implements {@link Actor} and provides basic movement management.
 */
public abstract class GameObject implements Actor {

    /**
     * The size of this game object (width and height).
     */
    private Vector size;

    /**
     * The current position of this game object.
     */
    private Vector position;

    /**
     * The velocity vector determining how this object moves each frame.
     */
    private Vector velocity;

    /**
     * Constructs a game object with specified size and position. <br>
     * Velocity is initialized to zero.
     *
     * @param size the size of the object
     * @param position the initial position of the object
     */
    public GameObject(Vector size, Vector position) {
        this(size, position, Vector.ZERO);
    }

    /**
     * Constructs a game object with specified size, position, and velocity.
     *
     * @param size the size of the object
     * @param position the initial position of the object
     * @param velocity the initial velocity of the object
     */
    public GameObject(Vector size, Vector position, Vector velocity) {
        this.size = size;
        this.position = position;
        this.velocity = velocity;
    }

    @Override
    public void act(GameContext context) {
        // Update position based on velocity each frame
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
