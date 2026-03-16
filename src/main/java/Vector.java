/**
 * Represents a 2D vector with x and y components. <br>
 * Immutable class used for positions, sizes, and velocities in the game.
 */
public class Vector {
    /**
     * A constant vector representing the zero vector (0, 0). <br>
     */
    public static final Vector ZERO = new Vector(0, 0);

    /**
     * The x-component of this vector.
     */
    private final double x;

    /**
     * The y-component of this vector.
     */
    private final double y;

    /**
     * Constructs a vector with the specified x and y components.
     *
     * @param x the x-component
     * @param y the y-component
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds another vector to this vector, returning a new vector that is the sum of the two vectors. <br>
     *
     * @param other the vector to add
     * @return a new vector representing the sum
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another vector from this vector, returning a new vector that is the difference of the two vectors. <br>
     *
     * @param other the vector to subtract
     * @return a new vector representing the difference
     */
    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    /**
     * Multiplies this vector by a scalar, returning a new vector that is the product of the vector and the scalar. <br>
     *
     * @param scalar the scalar to multiply by
     * @return a new vector representing the product
     */
    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
