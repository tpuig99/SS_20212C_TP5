import gravitational.Simulation4Jupiter;
import models.Body;
import models.BodyType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EJ3 {
    private static FileWriter fileWriter;
    private static PrintWriter printWriter;

    public static void main(String[] args) throws IOException {
        Body sun = new Body(0,0,0,0,1.989e30,70000000, BodyType.SUN);
        Body earth = new Body(1.500619962348151e8,2.288499248197072e6,-9.322979134387409e-1,2.966365033636722e1,5.97219e24,20000000,BodyType.EARTH);
        Body mars = new Body(-2.426617401833969e8,-3.578836154354768e7,4.435907910045917e0,-2.190044178514185e1,6.4171e23,10000000,BodyType.MARS);
        Body saturn = new Body(9.853710079799054E+08,-1.113456793790408E+09,6.704401108155684E+00,6.391237023898941E+00,5.6834e26,30000000,BodyType.SATURN);
        Body jupiter = new Body(6.500280253784848E+08,-3.745881860038198E+08,6.373758360629619E+00,1.194722075367566E+01,1.898E27,40000000,BodyType.JUPITER);
        double dt =  60*10;
        double tf = dt * 6* 24 * 365 * 5;

        double lounchPctg = 0.359;
        Simulation4Jupiter simulation = new Simulation4Jupiter("output.txt",dt,tf,lounchPctg,sun,earth,mars,jupiter,saturn);
        simulation.initializeSimulation();

        fileWriter = new FileWriter("datos3xJupiter.csv", false);
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

        if(simulation.spaceshipReachedJupiter()){
            System.out.println(simulation.getJupiterAnalisys());
        }
        printWriter.close();
        fileWriter.close();
        simulation.terminate();
    }



}
