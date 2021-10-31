package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.Circle;
import io.Event;
import io.InitialConditions;

import io.Output;
import models.Person;
import models.Vector;
import models.Versor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationImpl implements Simulation {

    private final String simulationFilename;
    private final String subcase;
    private final double gap;
    private final InitialConditions conds;
    private final double dt;
    private final double t_f;
    private double t;
    private double totalOuts;

    private List<Person> personlist;

    private  PrintWriter printWriter;

    private List<Event> events;
    private Resolver cr;

    public SimulationImpl(String simulationFilename, String subcase, InitialConditions conds, double gap, double dt, double t_f, double beta, double tau, double rMin, double rMax, double vdMax, double vExcape) {
        this.simulationFilename = simulationFilename;
        this.subcase = subcase;
        this.conds = conds;
        this.gap = gap;
        this.dt = dt;
        this.t_f = t_f;
        this.t = 0;
        this.totalOuts = 0;
        this.personlist = new ArrayList<>();
        this.cr = new Resolver(beta, tau, rMin, rMax, vdMax, vExcape, gap, conds);
    }

    @Override
    public void initializeSimulation() throws IOException {
        events = new ArrayList<>();
        printWriter = new PrintWriter(new BufferedWriter(new FileWriter(simulationFilename + ".xyz", false)));

        initializePersons();
        printXYZ(conds.getCircles());
    }

    private void initializePersons(){
        double id = 0;
        for(Circle circle: conds.getCircles()){
            Vector x = new Vector(circle.getX(), circle.getY());
            Versor e = cr.calculateDesiredVersor(x);
            Person newPerson = new Person(id,x, e, circle.getR(), 0);
            personlist.add(newPerson);
            id++;
        }
    }

    @Override
    public void nextIteration() {
        List<Person> newPersonList = personlist.stream().map(Person::clone).collect(Collectors.toList());
        for(Person person: newPersonList){
            List<Person> collisions = cr.calculateParticleCollision(person, personlist);
            if(collisions.size() == 0){
                cr.updateVelocityIfNotCollision(person);
                cr.updateRadiusIfNotCollision(person, dt);
                cr.updatePosition(person, dt);

            }else{
                cr.updateVelocityInCollisionWithPersons(person, collisions);
                cr.updateRadiusInCollisionWithPerson(person);
                cr.updatePosition(person, dt);
            }
        }
        personlist = newPersonList;
        t += dt;
        checkOuts();
    }

    private void checkOuts() {
        for(Person p :personlist){
            if(!p.isOut() && p.getX().getY()>conds.getLy()/2){
                p.setOut(true);
                p.setT_out(t);
                totalOuts ++;
            }
        }
    }

    @Override
    public void printIteration() {
        List<Circle> c = personlist.stream().map(Person::getAsCircle).collect(Collectors.toList());
        Event event = new Event(c,dt,t);
        printXYZ(c);
        events.add(event);
    }

    private void printXYZ(List<Circle> circles) {
        printWriter.println(circles.size());
        String headerProperties = String.format("Lattice=\"0.0 %s 0.0 0.0 %s 0.0 0.0 0.0\" Properties=pos:R:2:radius:R:1", conds.getLy(), conds.getLx());
        printWriter.println(headerProperties);
        for (Circle c : circles) {
            printWriter.println(c.xyz());
        }
    }

    @Override
    public boolean isFinished() {
        return totalOuts == personlist.size();
    }

    @Override
    public void terminate() throws IOException {
        try {
            final Output output = new Output(events, conds.getLx(), conds.getLy(),gap);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Paths.get(simulationFilename + "_light.json").toFile(), output);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        printWriter.close();
    }
}
