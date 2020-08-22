package org.firstinspires.ftc.teamcode.rework.Robot.pathfinding.geometry;

import java.util.HashSet;

public class Vector implements Comparable<Vector>
{
    /**
     * X-coordinate of the vector.
     */
    public double x;

    /**
     * Y-coordinate of the vector.
     */
    public double y;

    public double g; // The distance from here to our parent
    public double h; // The euclidian distance between here and the goal
    public double f; // The sum of g and h
    public final HashSet<Vector> neighbors = new HashSet<>();;
    public Vector parent;

    /**
     * Default constructor.
     */
    public Vector() {
        this(0, 0);
    }

    public Vector(Vector v) {
        this(v.x, v.y);
    }

    /**
     * Checks if a vector and this are the same.
     * @param o the other vector to check
     * @return true if their coordinates are the same
     */
    public boolean equals(Object o) {
        if (o instanceof Vector) {
            Vector v = (Vector) o;
            return x == v.x && y == v.y;
        } else {
            return false;
        }
    }

    /**
     * Builds a vector from its coordinates.
     * @param x x coordinate
     * @param y y coordinate
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a representative String of this vector.
     * @return a representative String of this vector.
     */
    public String toString() {
        return x + " : " + y;
    }

    /**
     * Adds another vector to this.
     * @param v vector to add.
     * @return this, after the addition.
     */
    public Vector add(Vector v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    /**
     * Subtracts another vector from this.
     * @param v vector to subtract.
     * @return this, after the subtraction.
     */
    public Vector subtract(Vector v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    /**
     * Multiplies this vector by a factor.
     * @param factor factor to multiply this vector by.
     * @return This vector, after the operation.
     */
    public Vector multiply(double factor) {
        x *= factor;
        y *= factor;
        return this;
    }


    /**
     * Gets the square value of the parameter passed.
     * @param x parameter
     * @return x * x
     */
    public static double square(double x) {
        return x * x;
    }

    /**
     * Returns the square distance from this vector to the one in the arguments.
     * @param v the other vector, to calculate the distance to.
     * @return the square distance, in pixels, between this vector and v.
     */
    public double squareDistance(Vector v) {
        return square(x - v.x) + square(y - v.y);
    }

    /**
     * Gets the magnitude of the vector.
     * @return the magnitude of the vector (Math.sqrt(sqr(x) + sqr(y)))
     */
    public double magnitude() {
        return Math.sqrt(square(x) + square(y));
    }

    /**
     * Returns the distance from this vector to the one in the arguments.
     * @param v the other vector, to calculate the distance to.
     * @return the distance, in pixels, between this vector and v.
     */
    public double distance(Vector v) {
        return Math.sqrt(squareDistance(v));
    }


    /**
     * Normalises this vector.
     */
    public void normal() {
        double mag = magnitude();
        if(mag == 0)
        {
            x = y = 0;
        }else{
            x /= mag;
            y /= mag;
        }
    }

    /**
     * Calculates the dot product between this vector and the one passed by parameter.
     * @param v the other vector.
     * @return the dot product between these two vectors.
     */
    public double dotProduct(Vector v) {
        double tot = this.x * v.x + this.y * v.y;
        return tot;
    }


    public void update(Vector vector) {
        double newG = vector.g + distance(vector);
        if (newG < g) {
            parent = vector;
            g = newG;
            f = g + h;
        }
    }

    /**
     * We're calculating the cost of the estimated shortest path from here to the target (heuristic/euclidian distance)
     * plus the actual distance from here to the start (which is passed down through constructors, and starts at zero).
     * This is what our PriorityQueue will use to determine which path to extend. In constrast, Djikstra's algorithm
     * only sorts based on the distance from the start to here, which in dense graphs, can end up just floodfilling
     * the entire graph.
     * @param target
     */
    public void calculateCosts(Vector target) {
        h = distance(target);
        f = g + h;
    }


    @Override
    public int compareTo(Vector other) {
        if (other.f < f)
            return 1;
        return -1;
    }


}
