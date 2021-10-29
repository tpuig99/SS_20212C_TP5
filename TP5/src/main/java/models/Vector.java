package models;

import java.util.Objects;

public class Vector {

    private double x, y;

    public Vector (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector (double x, double y, double module) {
        this.x = x * module;
        this.y = y * module;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double norm() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector unitaryVector() {
        return normalizeVector(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + '}';
    }

    public static Vector normalizeVector (Vector v) {
        return new Vector(v.getX() / v.norm(), v.getY() / v.norm());
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }
}
