package simulation;

import io.Circle;
import io.InitialConditions;

import models.Person;
import oscillators.IntegrationScheme;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SimulationImpl implements Simulation {

    private final IntegrationScheme scheme;
    private final String simulationFilename;
    private final double gap;
    private final InitialConditions conds;
    private final double dt;
    private final double t_f;
    private double t;

    private List<Person> personlist;

    private FileWriter fileWriter;
    private  PrintWriter printWriter;

    public SimulationImpl(IntegrationScheme scheme, String simulationFilename, InitialConditions conds, double gap, double dt, double t_f) {
        this.scheme = scheme;
        this.simulationFilename = simulationFilename;
        this.conds = conds;
        this.gap = gap;
        this.dt = dt;
        this.t_f = t_f;
        this.t = 0;
        this.personlist = new ArrayList<>();
    }

    @Override
    public void initializeSimulation() throws IOException {
        fileWriter = new FileWriter(simulationFilename, false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));

        initializePersons();

        printWriter.println(t);
        printWriter.println(scheme.getOscillator());
    }

    private void initializePersons(){
        for(Circle circle: conds.getCircles()){
            Point2D r = new Point2D.Double(circle.getX(), circle.getY());
            Point2D e = calculateDesiredVersor(r, gap, conds.getLx(), conds.getLy(), circle.getR());
            Person newPerson = new Person(r, e, 0, circle.getR());
            personlist.add(newPerson);
        }
    }

    private Point2D calculateDesiredVersor(Point2D r, double gap, double lx, double ly, double radius) {
        Point2D targetPoint;
        if(r.getX() < (lx/2 - gap/2)){
            targetPoint = new Point2D.Double((lx/2 - gap/2) + radius,ly/2);
        }else if(r.getX() > (lx/2 + gap/2)){
            targetPoint = new Point2D.Double((lx/2 + gap/2) - radius,ly/2);
        }else{
            targetPoint = new Point2D.Double(r.getX(),ly/2);
        }
        return calculateVersorFromPointToPoint(r, targetPoint);
    }

    private Point2D calculateVersorFromPointToPoint(Point2D p1, Point2D p2){
        double x = p1.getX()-p2.getX();
        double y = p1.getY()-p2.getY();
        double mod = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        return new Point2D.Double(x/mod, y/mod);
    }

    @Override
    public void nextIteration() {
        double r = scheme.calculatePosition(dt);
        double v = scheme.calculateVelocity(dt);
        scheme.updateOscillator(r, v);

        t += dt;

        analytics.add(analyticSolution.calculatePosition(t));
        values.add(scheme.getOscillator().getR());
    }

    @Override
    public void printIteration() {
        printWriter.println((float) t);
        printWriter.println(scheme.getOscillator());
    }

    @Override
    public boolean isFinished() {
        return t_f < t;
    }

    @Override
    public void terminate() throws IOException {
        printWriter.close();
        fileWriter.close();
    }
}
