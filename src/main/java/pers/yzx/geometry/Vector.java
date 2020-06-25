package pers.yzx.geometry;

/**
 * vector class implementation.
 */
public class Vector {
    public double x;
    public double y;
    public double z;

    /**
     * Constructs a new vector from origin to terminal
     *
     * @param origin   origin point of the new vector
     * @param terminal terminal point of the new vector
     */
    public Vector(Point origin, Point terminal) {
        this.x = terminal.getX() - origin.getX();
        this.y = terminal.getY() - origin.getY();
        this.z = terminal.getZ() - origin.getZ();
    }

    /**
     * Constructs a new vector
     *
     * @param x The x coordinate of the new vector
     * @param y The y coordinate of the new vector
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a new 2D vector
     *
     * @param x The x coordinate of the new vector
     * @param y The y coordinate of the new vector
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * Returns a new instance holding the result of the vector addition
     *
     * @param vector The vector to be added
     * @return A new instance holding the result of the vector addition
     */
    public Vector add(Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * Returns a new instance holding the result of the vector subtraction
     *
     * @param vector The vector to be subtracted
     * @return A new instance holding the result of the vector subtraction
     */
    public Vector sub(Vector vector) {
        return new Vector(this.x - vector.x, this.y - vector.y, this.z - z);
    }

    /**
     * Computes the magnitude or this.
     *
     * @return The magnitude of this
     */
    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Multiplies this by the given scalar.
     *
     * @param scalar The scalar to be multiplied by this
     * @return A new instance holding the result of the multiplication
     */
    public Vector mult(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    /**
     * Computes the dot product of this and the given vector.
     *
     * @param vector The vector to be multiplied by this
     * @return A new instance holding the result of the multiplication
     */
    public double dot(Vector vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    /**
     * Returns the cross product vector of this and the given vector.
     *
     * @param vector The vector to be multiplied to the perpendicular vector of this
     * @return A new instance holding the result of the cross product
     */
    public Vector cross(Vector vector) {
        return new Vector(this.y * vector.z - this.z * vector.y, this.x * vector.z - this.z * vector.x,
                this.x * vector.y - this.y * vector.x);
    }

    /**
     * Computes the 2D pseudo cross product Dot(Perp(this), vector) of this and
     * the given vector.
     *
     * @param vector The vector to be multiplied to the perpendicular vector of this
     * @return the pseudo cross product
     */
    public double pseudoCross(Vector vector) {
        return this.x * vector.y - this.y * vector.x;
    }

    @Override
    public String toString() {
        return "Vector2D[" + x + ", " + y + "]";
    }

    public double angle(Vector vector) {
        return Math.acos(this.dot(vector)/ (this.mag()*vector.mag()));
    }
}