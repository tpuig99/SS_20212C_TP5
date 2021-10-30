package models;

public class Versor extends Vector {

    public Versor() {
        super();
    }

    public Versor(double x, double y) {
        super(x, y);
        double norm = calculateVersorNorm(x, y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    public void updateXY(double x, double y) {
        double norm = calculateVersorNorm(x, y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    public void updateX(double x) {
        double y = super.getY();
        double norm = calculateVersorNorm(x, y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    public void updateY(double y) {
        double x = super.getX();
        double norm = calculateVersorNorm(x, y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    private double calculateVersorNorm(double x, double y) {
        if (Double.compare(x, 0) == 0 && Double.compare(y, 0) == 0) {
            return 1;
        }
        return Math.sqrt(x*x + y*y);
    }
}
