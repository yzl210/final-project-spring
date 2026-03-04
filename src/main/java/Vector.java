public record Vector(double x, double y) {
    public static final Vector ZERO = new Vector(0, 0);

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        double mag = magnitude();
        if (mag == 0) return ZERO;
        return new Vector(x / mag, y / mag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector vector) {
            return x == vector.x && y == vector.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vector[" + "x=" + x + ", " + "y=" + y + ']';
    }

}
