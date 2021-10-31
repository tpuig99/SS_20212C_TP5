package simulation;

import io.InitialConditions;
import models.Person;
import models.Vector;
import models.Versor;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Resolver {

    private final double beta;
    private final double tau;
    private final double rMin, rMax, vdMax,gap,vEscape;
    private InitialConditions conds;

    public Resolver(double beta, double tau, double rMin, double rMax, double vdMax, double vEscape, double gap, InitialConditions conds) {
        this.beta = beta;
        this.tau = tau;
        this.rMin = rMin;
        this.rMax = rMax;
        this.vdMax = vdMax;
        this.gap = gap;
        this.vEscape = vEscape;
        this.conds = conds;
    }

    private enum Wall { N, L, R, T, B, O };

    public void updatePosition(Person p1,double dt){
        double X_x = p1.getX().getX() + p1.getE().getX()* p1.getVd() * dt;
        double X_y = p1.getX().getY() + p1.getE().getY()* p1.getVd() * dt;
        p1.updatePosition(X_x, X_y);
    }

    public void updateVelocityIfNotCollision(Person p1){
        double aux = (p1.getR()-rMin)/(rMax - rMin);
        double vd = vdMax * Math.pow(aux,beta);
        p1.setVd(vd);
        Versor v = calculateDesiredVersor(p1.getX());
        p1.setE(v);
    }

    public void updateRadiusIfNotCollision(Person p1,double dt){
       if(p1.getR() < rMax){
           double nextR = p1.getR() + rMax / (tau / dt);
           p1.setR(Math.min(nextR,rMax));
       }
    }

    public void updateVelocityInCollisionWithPersons(Person p1, List<Person> other) {
        p1.setVd(vEscape);
        Versor ve = new Versor();
        for (Person p : other) {
            Versor aux = calculateVersorFromPointToPoint(p.getX(),p1.getX());
            Vector added = Vector.add(ve,aux);
            ve.updateXY(added.getX(),added.getY());
        }
        p1.setE(ve);
    }

    public void updateRadiusInCollisionWithPerson(Person p1) {
        p1.setR(rMin);
    }

    public Versor calculateDesiredVersor(Vector pos) {
        Vector targetPoint;
        double xe1 = (conds.getLx()/2 - gap/2);
        double xe2 = (conds.getLx()/2 + gap/2);
        double L = xe2 - xe1;
        double x = pos.getX();
        double e = conds.getLy() * 0;
        if(pos.getY() + e >= conds.getLy()/2){
            targetPoint = new Vector(x, conds.getLy());
        }else{
            if(x < (xe1 + 0.2*L) || x > (xe1 + 0.8*L)){
                double max = xe1+0.8*L;
                double min = xe1+0.2*L;
                targetPoint = new Vector((Math.random()*(max-min))+min, conds.getLy()/2);
            }else{
                targetPoint = new Vector(x,conds.getLy()/2);
            }
        }

        return calculateVersorFromPointToPoint(pos, targetPoint);
    }

    private Versor calculateVersorFromPointToPoint(Vector p1, Vector p2){
        return new Versor(p2.getX()-p1.getX(), p2.getY()-p1.getY());
    }


    public List<Person> calculateParticleCollision(Person p1, List<Person> personList) {
        List<Person> collisions = new ArrayList<>();
        for (Person p:personList) {
            if(p1.getId() != p.getId()) {
                double d = Point2D.distance(p1.getX().getX(), p1.getX().getY(), p.getX().getX(), p.getX().getY())
                        - p.getR() - p1.getR();
                if (d < 0) {
                    collisions.add(p);
                }
            }
        }
        Wall checkX = checkCollisionInX(p1);
        if(checkX == Wall.R){
            collisions.add(new Person(-1,new Vector(conds.getLx(),p1.getX().getY()),new Versor(0,0),0,0));
        } else if(checkX == Wall.L){
            collisions.add(new Person(-2,new Vector(0,p1.getX().getY()),new Versor(0,0),0,0));
        }

        Wall checkY = checkCollisionInY(p1);
        if(checkY == Wall.B){
            collisions.add(new Person(-3,getVectorForGapCollision(p1),new Versor(0,0),0,0));
        } else if(checkY == Wall.T){
            collisions.add(new Person(-4,new Vector(p1.getX().getX(),0),new Versor(0,0),0,0));
        }
        return collisions;
    }

    private Vector getVectorForGapCollision(Person p1) {
        double xe1 = (conds.getLx()/2 - gap/2);
        double xe2 = (conds.getLx()/2 + gap/2);
        if(p1.getX().getX() < xe1 || p1.getX().getX() > xe2)
            return new Vector(p1.getX().getX(),conds.getLy()/2);
        return p1.getX().getX() < conds.getLx()/2 ? new Vector(xe1,conds.getLy()/2) : new Vector(xe2,conds.getLy()/2);
    }

    private Wall checkCollisionInX(Person p1) {
        if(conds.getLx() - p1.getX().getX() < p1.getR()){
            return Wall.R;
        }

        if(p1.getX().getX() < p1.getR()){
            return Wall.L;
        }
        return Wall.N;
    }

    private Wall checkCollisionInY(Person p1) {
        boolean ifNotInGap = (p1.getX().getX() - p1.getR())<(conds.getLx()/2-gap/2)|| (p1.getX().getX() + p1.getR())>(conds.getLx()/2+gap/2);
        if(ifNotInGap && p1.getX().getY()<conds.getLy()/2 && conds.getLy()/2 - p1.getX().getY() < p1.getR()){
            return Wall.B;
        }

        if(p1.getX().getY() < p1.getR()){
            return Wall.T;
        }
        return Wall.N;
    }
}
