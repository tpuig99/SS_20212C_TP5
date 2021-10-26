package simulation;

import models.Direction;
import models.Particle;

public class CollisionResolver {

    private final double Lx;
    private final double gapSize;
    private final double wallSize;


    public CollisionResolver(double Lx, double gapSize, double wallSize) {
        this.Lx = Lx;
        this.gapSize = gapSize;
        this.wallSize = wallSize;
    }

    public void updateVelocityInCollisionWithParticle(Particle p1, Particle p2, int Jm) {
        final double dX, dY, dVx, dVy, sigma, dVdR, J, Jx, Jy;

        dX = Jm*(p2.getX() - p1.getX());
        dY = Jm*(p2.getY() - p1.getY());
        dVx = Jm*(p2.getVx() - p1.getVx());
        dVy = Jm*(p2.getVy() - p1.getVy());

        sigma = p1.getR() + p2.getR();
        dVdR = dVx * dX + dVy * dY;
        J = dVdR / sigma;

        Jx = (J * dX) / sigma;
        Jy = (J * dY) / sigma;

        double vx_after = p1.getVx() + Jm * Jx;
        double vy_after = p1.getVy() + Jm * Jy;
        double speed_a = Math.sqrt(Math.pow(vx_after, 2) + Math.pow(vy_after, 2));
        double speed_b = p1.calculateSpeedModule();

        p1.updateVelocity(p1.getVx() + Jm * Jx, p1.getVy() + Jm * Jy);
    }

    public void updateVelocityInCollisionWithRoof(Particle p, Direction direction) {
        if (direction == Direction.R || direction == Direction.L || direction == Direction.M) {
            p.updateVelocity(-p.getVx(), p.getVy());
        } else if (direction == Direction.T || direction == Direction.B) {
            p.updateVelocity(p.getVx(), -p.getVy());
        }
    }

    private double getTc(double dX, double dY, double dVx, double dVy, double sigma ) {
        final double  dVdR, dVdV, dRdR, d;

        dVdR = dVx * dX + dVy * dY;

        if (dVdR < 0) {
            dVdV = dVx * dVx + dVy * dVy;
            dRdR = dX * dX + dY * dY;
            d = Math.pow(dVdR, 2) - dVdV * (dRdR - Math.pow(sigma, 2));

            if (d >= 0) {
                return - (dVdR + Math.sqrt(d)) / dVdV;
            }
        }
        return -1;
    }

    public void updateVelocityInCollisionWithVertex(Particle p, double yp) {
        final double dX, dY, dVx, dVy, sigma, dVdR, J, Jx, Jy;
        dX = (Lx / 2 - p.getX());
        dY = (yp - p.getY());
        dVx = (- p.getVx());
        dVy = (- p.getVy());

        sigma = p.getR();
        dVdR = dVx * dX + dVy * dY;
        J = 2 * dVdR / sigma;

        Jx = (J * dX) /  sigma;
        Jy = (J * dY) /  sigma;

        p.updateVelocity(p.getVx() + Jx, p.getVy() + Jy);
    }

    public double calculateParticleCollision(Particle p1, Particle p2) {
        return getTc(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getVx() - p1.getVx(),
                p2.getVy() - p1.getVy(), p1.getR() + p2.getR());
    }

    public double calculateCollisionWithTopVertex(Particle p) {
        return getTc((Lx/2) - p.getX(), wallSize + gapSize - p.getY(), - p.getVx(), - p.getVy(), p.getR());
    }

    public double calculateCollisionWithBottomVertex(Particle p) {
        return getTc((Lx/2) - p.getX(), wallSize - p.getY(), - p.getVx(), - p.getVy(), p.getR());
    }

}
