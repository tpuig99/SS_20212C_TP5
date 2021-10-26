package gravitational;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.Event;
import io.Output;
import models.Analisys;
import models.Body;
import models.BodyType;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation5Jupiter implements simulation.Simulation {

    private BeemanGravitational schemeEarth;
    private BeemanGravitational schemeSpaceship;
    private BeemanGravitational schemeMars;
    private BeemanGravitational schemeJupiter;
    private BeemanGravitational schemeSaturn;

    private final String simulationFilename;
    private double t;
    private final double dt;
    private final double t_f;
    private final double lounchPctg;

    private final Body sun;
    private final Body earth;
    private final Body mars;
    private final Body jupiter;
    private final Body saturn;

    private Body spaceship;
    private List<Event> events;
    private double spaceshipVel;

    private FileWriter fileWriter;
    private  PrintWriter printWriter;

    private boolean spaceShipInitialized = false;
    private boolean spaceshipReachedJupiter;
    private double spaceshipJupiterMinDist;
    private double spaceshipJupiterMinDistCurrDist;
    private double jupiterRadius = 69911 + 16690; //radio + luna más lejana = calisto

    private double spaceshipTimeOfArrival = -1;
    private boolean noResult = false;

    public Simulation5Jupiter(String simulationFilename, double dt, double t_f, double lounchPctg, Body sun, Body earth, Body mars,Body jupiter, Body saturn, double spaceshipVel) {
        this.simulationFilename = simulationFilename;
        this.t = 0;
        this.dt = dt;
        this.t_f = t_f;
        this.lounchPctg = lounchPctg;
        this.sun = sun;
        this.earth = earth;
        this.mars = mars;
        this.jupiter = jupiter;
        this.saturn = saturn;
        this.spaceship = null;
        this.spaceshipVel = spaceshipVel;

        spaceshipReachedJupiter = false;
        spaceshipJupiterMinDistCurrDist = jupiter.getR().distance(earth.getR());
        spaceshipJupiterMinDist = spaceshipJupiterMinDistCurrDist;
    }

    @Override
    public void initializeSimulation() throws IOException {
        events = new ArrayList<>();

        schemeEarth = new BeemanGravitational(earth, Arrays.asList(sun,mars,jupiter,saturn));
        schemeMars = new BeemanGravitational(mars, Arrays.asList(sun,earth,jupiter,saturn));
        schemeSaturn = new BeemanGravitational(saturn, Arrays.asList(sun,mars,earth,jupiter));
        schemeJupiter = new BeemanGravitational(jupiter, Arrays.asList(sun,earth,mars,saturn));

        fileWriter = new FileWriter(simulationFilename.replace(".txt", ".xyz"), false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));
    }

    @Override
    public void nextIteration() {

        if(t/t_f > lounchPctg && !spaceShipInitialized){
            spaceShipInitialized = true;
            spaceship = initializeSpaceship(earth,sun);
            schemeSpaceship = new BeemanGravitational(spaceship, Arrays.asList(mars, sun,earth,jupiter,saturn));
        }

        //System.out.println(t/(60*60*24));

        if(spaceShipInitialized){
            double auxDist = jupiter.getR().distance(spaceship.getR());
            double newSpaceshipJupiterMinDist = Math.min(auxDist, spaceshipJupiterMinDist);
            if(newSpaceshipJupiterMinDist < spaceshipJupiterMinDist){
                spaceshipJupiterMinDist = newSpaceshipJupiterMinDist;
                if(spaceshipJupiterMinDist < jupiterRadius) {
                    //System.out.println(auxDist + " " + t/(60*60*24) + " " + (lounchPctg*t_f)/(60*60*24));
                    spaceshipReachedJupiter = true;
                    spaceshipTimeOfArrival = t;
                    System.out.println("Le pegamos.");
                }
            }

            double auxDistToSunFromSpaceship = sun.getR().distance(spaceship.getR());
            double auxDistToSunFromJupiter = sun.getR().distance(jupiter.getR());
            if(auxDistToSunFromSpaceship > auxDistToSunFromJupiter){
                noResult = true;
            }
        }

        if(spaceShipInitialized) {
            Point2D r_spaceship = schemeSpaceship.calculatePosition(dt);
            Point2D v_spaceship = schemeSpaceship.calculateVelocity(dt);
            schemeSpaceship.updateBody(r_spaceship, v_spaceship);
            spaceship.setR(r_spaceship);
            spaceship.setV(v_spaceship);
        }

        Point2D r_earth = schemeEarth.calculatePosition(dt);
        Point2D v_earth = schemeEarth.calculateVelocity(dt);
        Point2D r_mars = schemeMars.calculatePosition(dt);
        Point2D v_mars = schemeMars.calculateVelocity(dt);
        Point2D r_jupiter = schemeJupiter.calculatePosition(dt);
        Point2D v_jupiter = schemeJupiter.calculateVelocity(dt);
        Point2D r_saturn = schemeSaturn.calculatePosition(dt);
        Point2D v_saturn = schemeSaturn.calculateVelocity(dt);

        schemeEarth.updateBody(r_earth, v_earth);
        schemeMars.updateBody(r_mars, v_mars);
        schemeJupiter.updateBody(r_jupiter,v_jupiter);
        schemeSaturn.updateBody(r_saturn,v_saturn);

        earth.setR(r_earth);
        earth.setV(v_earth);
        mars.setR(r_mars);
        mars.setV(v_mars);
        jupiter.setR(r_jupiter);
        jupiter.setV(v_jupiter);
        saturn.setR(r_saturn);
        saturn.setV(v_saturn);

        t += dt;
    }

    private Body initializeSpaceship(Body earth,Body sun) {
        double earthRadio = 6371.01;
        double spaceshipHigh = 1500;
        double spaceshipR = earthRadio + spaceshipHigh;
        Point2D earthSpaceshipNVector = sun.calculateNormalR(earth);

        Body spaceship = new Body(earth.getR().getX() + earthSpaceshipNVector.getX() * spaceshipR,earth.getR().getY() + earthSpaceshipNVector.getY() * spaceshipR,0,0,2E5, 10000000.0,BodyType.SPACESHIP);

        //System.out.println(spaceship.getR().distance(earth.getR()));

        double spaceshipVelocity = spaceshipVel;
        double stationVelocity = 7.12;
        Point2D velocityTVector = earth.calculateTVector(spaceship);

        double spaceshipV = earth.calculateV()+ spaceshipVelocity + stationVelocity;
        spaceship.setV(new Point2D.Double(velocityTVector.getX()*spaceshipV,velocityTVector.getY()*spaceshipV));

        return spaceship;
    }

    @Override
    public void printIteration() throws IOException {
        Event event;
        if(spaceship != null){
            event = new Event(Arrays.asList(earth.getAsCircle(),mars.getAsCircle(),sun.getAsCircle(),spaceship.getAsCircle(),jupiter.getAsCircle(),saturn.getAsCircle()),dt,t);
        }else{
            event = new Event(Arrays.asList(earth.getAsCircle(),mars.getAsCircle(),sun.getAsCircle(),jupiter.getAsCircle(),saturn.getAsCircle()),dt,t);
        }
        events.add(event);

        printXYZ();
    }

    private void printXYZ() {
        printWriter.println(4);
        printWriter.println();
        for (Body b: Arrays.asList(sun,earth,mars,jupiter,saturn)) {
            printWriter.println(b.xyz());
        }
        if(spaceship!=null){
            printWriter.println(spaceship.xyz());
        } else {
            String s = String.format("%s %f %f %f",BodyType.SPACESHIP, earth.getR().getX()+sun.calculateNormalR(earth).getX()* earth.getRadio(),earth.getR().getY()+sun.calculateNormalR(earth).getY()* earth.getRadio(),10000000.0);
            printWriter.println(s);
        }
    }

    @Override
    public double calculateECM() {
        return 0;
    }

    @Override
    public boolean isFinished() {
        return t_f < t || noResult;
    }

    @Override
    public void terminate() throws IOException {
        try {
            final Output output = new Output(events);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Paths.get(simulationFilename.replace(".txt", "_light.json")).toFile(), output);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        printWriter.close();
        fileWriter.close();
        //System.out.println(spaceshipMarsMinDist);
    }
    public boolean spaceshipReachedJupiter(){
        return spaceshipReachedJupiter;
    }
    public double getSpaceshipJupiterMinDist(){return spaceshipJupiterMinDist;}
    public Analisys getJupiterAnalisys(){
        return new Analisys(spaceshipJupiterMinDist, spaceshipTimeOfArrival, lounchPctg, t_f);
    }
    public String getData(){
        return spaceshipVel + ", "
                + spaceshipJupiterMinDist + ", "
                + spaceshipTimeOfArrival + ", "
                + t_f + ", "
                + (lounchPctg * t_f)/(60 * 60 * 24);
    }
    public boolean isSpaceShipInitialized(){
        return spaceShipInitialized;
    }
    public String getSpaceShipData(){
        double spaceshipVelocity = Math.sqrt(Math.pow(spaceship.getV().getX(),2) + Math.pow(spaceship.getV().getY(),2));
        return  spaceshipVelocity + ", "
                + (t)/(60 * 60 * 24);
    }
}
