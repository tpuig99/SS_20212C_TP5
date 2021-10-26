package gravitational;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.Event;
import io.Output;
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

public class Simulation3 implements simulation.Simulation {

    private BeemanGravitational schemeEarth;
    private BeemanGravitational schemeSpaceship;
    private BeemanGravitational schemeMars;
    private final String simulationFilename;
    private double t;
    private final double dt;
    private final double t_f;
    private final double lounchPctg;

    private Body sun;
    private Body earth;
    private Body mars;
    private Body spaceship;
    private List<Event> events;

    private FileWriter fileWriter;
    private  PrintWriter printWriter;

    private boolean spaceShipInitialized = false;
    private double marsDist = -1;

    public Simulation3(String simulationFilename, double dt, double t_f, double lounchPctg, Body sun, Body earth, Body mars) {
        this.simulationFilename = simulationFilename;
        this.t = 0;
        this.dt = dt;
        this.t_f = t_f;
        this.lounchPctg = lounchPctg;
        this.sun = sun;
        this.earth = earth;
        this.mars = mars;
        this.spaceship = null;
        marsDist = earth.getR().distance(mars.getR());
    }

    @Override
    public void initializeSimulation() throws IOException {
        events = new ArrayList<>();
        schemeEarth = new BeemanGravitational(earth, Arrays.asList(sun,mars));
        schemeMars = new BeemanGravitational(mars, Arrays.asList(sun,earth));

        fileWriter = new FileWriter(simulationFilename.replace(".txt", ".xyz"), false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));
    }

    @Override
    public void nextIteration() {

        if(t/t_f > lounchPctg && !spaceShipInitialized){
            spaceShipInitialized = true;
            spaceship = initializeSpaceship(earth,sun);
            schemeSpaceship = new BeemanGravitational(spaceship, Arrays.asList(mars,sun,earth));
        }

        if(spaceShipInitialized) {
            double auxDist = mars.getR().distance(spaceship.getR());
            marsDist = Math.min(auxDist, marsDist);

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

        schemeEarth.updateBody(r_earth, v_earth);
        schemeMars.updateBody(r_mars, v_mars);

        earth.setR(r_earth);
        earth.setV(v_earth);

        mars.setR(r_mars);
        mars.setV(v_mars);

        t += dt;
    }

    private static Body initializeSpaceship(Body earth,Body sun) {
        double earthRadio = 6371.01;
        double spaceshipHigh = 1500;
        double spaceshipR = earthRadio + spaceshipHigh;
        Point2D earthSpaceshipNVector = sun.calculateNormalR(earth);

        Body spaceship = new Body(earth.getR().getX() + earthSpaceshipNVector.getX() * spaceshipR,earth.getR().getY() + earthSpaceshipNVector.getY() * spaceshipR,0,0,2E5, 0,BodyType.SPACESHIP);

        System.out.println(spaceship.getR().distance(earth.getR()));

        double spaceshipVelocity = 8;
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
            event = new Event(Arrays.asList(earth.getAsCircle(),mars.getAsCircle(),sun.getAsCircle(),spaceship.getAsCircle()),dt,t);
        }else{
            event = new Event(Arrays.asList(earth.getAsCircle(),mars.getAsCircle(),sun.getAsCircle()),dt,t);
        }
        events.add(event);

        printXYZ();
    }

    private void printXYZ() {
        printWriter.println(4);
        printWriter.println();
        for (Body b: Arrays.asList(sun,earth,mars)) {
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
        return t_f < t;
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
    }
}
