import gravitational.Simulation4;
import gravitational.Simulation5;
import models.Analisys;
import models.Body;
import models.BodyType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EJ2c {

    private static FileWriter fileWriter;
    private static PrintWriter printWriter;

    public static void main(String[] args) throws IOException {
        Body sun = new Body(0,0,0,0,1.989e30,70000000, BodyType.SUN);
        Body earth = new Body(1.500619962348151e8,2.288499248197072e6,-9.322979134387409e-1,2.966365033636722e1,5.97219e24,20000000,BodyType.EARTH);
        Body mars = new Body(-2.426617401833969e8,-3.578836154354768e7,4.435907910045917e0,-2.190044178514185e1,6.4171e23,10000000,BodyType.MARS);
        double dt =  60*10;
        double tf = dt * 6* 24 * 365 * 2;

        double i = 23.7;
        double step = 0.01;
        double end = 24.5;
        List<Analisys> mins = new ArrayList<>();

        fileWriter = new FileWriter("datos6p4.csv", false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));

        double lounchPctg = 0.48895;

        while(i<end){
            double spaceshipVel = i;

            sun = new Body(0,0,0,0,1.989e30,70000000, BodyType.SUN);
            earth = new Body(1.500619962348151e8,2.288499248197072e6,-9.322979134387409e-1,2.966365033636722e1,5.97219e24,20000000,BodyType.EARTH);
            mars = new Body(-2.426617401833969e8,-3.578836154354768e7,4.435907910045917e0,-2.190044178514185e1,6.4171e23,10000000,BodyType.MARS);

            Simulation5 simulation = new Simulation5("output.txt",dt,tf,lounchPctg,sun,earth,mars,spaceshipVel);
            simulation.initializeSimulation();

            while (!simulation.isFinished()) {
                simulation.nextIteration();
                try {
                    simulation.printIteration();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            simulation.terminate();

            System.out.println(simulation.getData());
            printWriter.println(simulation.getData());

//            if(simulation.spaceshipReachedMars()){
//                System.out.println(simulation.getMarsAnalisys());
//            }
            i=i+step;
        }
        printWriter.close();
        fileWriter.close();
    }



}
