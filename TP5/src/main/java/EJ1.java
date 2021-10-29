import com.fasterxml.jackson.databind.ObjectMapper;
import io.InitialConditions;
import simulation.SimulationImpl;
import simulation.Simulation;

import java.io.*;
import java.nio.file.Paths;

public class EJ1 {

    private static double dt = 1e-5;
    private static double dt2 = 1e-2;
    private static double gap = 1.2;
    private static double t_f = 100;
    private static double beta = 0.9;
    private static double tau = 0.5;
    private static double rMin = 0.15;
    private static double rMax = 0.32;
    private static double vdMax = 2;
    private static double vEscape = 1.55;
    private static String initialConditionsFilename;
    private static String simulationFilename;

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Wrong amount of arguments");
            return;
        }

        InitialConditions conds = parseArguments(args);

        doSimulation(conds);
    }

    public static InitialConditions parseArguments(String[] args) {

        initialConditionsFilename = args[0];
        simulationFilename = args[1];

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(initialConditionsFilename).toFile(), InitialConditions.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String fmt (double dt) {
        return String.format("%f", dt).replace(',', '.');
    }

    public static void doSimulation(InitialConditions conds) throws IOException {

        dt = rMin / 2*Math.max(vdMax, vEscape);
        dt2 = 10 * dt;
        t_f = 1000 * dt;
        Simulation simulations = new SimulationImpl(simulationFilename, conds, gap, dt, t_f, beta, tau, rMin, rMax, vdMax, vEscape);

        double counter = 0;
        simulations.initializeSimulation();
        while (!simulations.isFinished()) {
            simulations.nextIteration();
            counter += dt;
            if(counter >= dt2){
                try {
                    simulations.printIteration();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter = 0;
            }
        }
        simulations.terminate();

    }
}
