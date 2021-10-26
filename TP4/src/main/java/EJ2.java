import gravitational.Simulation3;
import gravitational.Simulation4;
import models.Body;
import models.BodyType;
import simulation.OscilatorSimulation;
import simulation.Simulation;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EJ2 {

    private static FileWriter fileWriter;
    private static PrintWriter printWriter;

    public static void main(String[] args) throws IOException {
        Body sun = new Body(0,0,0,0,1.989e30,70000000, BodyType.SUN);
        Body earth = new Body(1.500619962348151e8,2.288499248197072e6,-9.322979134387409e-1,2.966365033636722e1,5.97219e24,20000000,BodyType.EARTH);
        Body mars = new Body(-2.426617401833969e8,-3.578836154354768e7,4.435907910045917e0,-2.190044178514185e1,6.4171e23,10000000,BodyType.MARS);
        double dt =  60*10;
        double tf = dt * 6* 24 * 365 * 2;

        double lounchPctg = 0.48895;
        Simulation4 simulation = new gravitational.Simulation4("output.txt",dt,tf,lounchPctg,sun,earth,mars);
        simulation.initializeSimulation();

        fileWriter = new FileWriter("datos3vis.csv", false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));

        while (!simulation.isFinished()) {
            simulation.nextIteration();
            try {
                simulation.printIteration();
                if(simulation.isSpaceShipInitialized()){
                    System.out.println(simulation.getSpaceShipData());
                    printWriter.println(simulation.getSpaceShipData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(simulation.spaceshipReachedMars()){
            System.out.println(simulation.getMarsAnalisys());
        }
        printWriter.close();
        fileWriter.close();
        simulation.terminate();
    }



}
