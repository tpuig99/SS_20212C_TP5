package models;

public class Versor extends Vector {

    public Versor(double x, double y) {
        super(x, y);
        double norm = Math.sqrt(x*x + y*y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    public void updateXY(double x, double y) {
        double norm = Math.sqrt(x*x + y*y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    public void updateX(double x) {
        double y = super.getY();
        double norm = Math.sqrt(x*x + y*y);
        super.setX(x / norm);
        super.setY(y / norm);
    }

    public void updateY(double y) {
        double x = super.getX();
        double norm = Math.sqrt(x*x + y*y);
        super.setX(x / norm);
        super.setY(y / norm);
    }
}
