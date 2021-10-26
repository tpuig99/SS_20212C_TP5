import com.fasterxml.jackson.databind.ObjectMapper;
import io.InitialConditions;
import models.Person;
import oscillators.*;
import simulation.SimulationImpl;
import simulation.Simulation;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EJ1 {

    private static double dt = 1e-5;
    private static double gap;
    private static double t_f;
    private static String initialConditionsFilename;
    private static String simulationFilename;

    public static void main(String[] args) throws IOException {

        if (args.length != 4) {
            System.err.println("Wrong amount of arguments");
            return;
        }

        InitialConditions conds = parseArguments(args);

        doSimulation(conds);
    }

    public static InitialConditions parseArguments(String[] args) {

        initialConditionsFilename = args[0];
        gap = Double.parseDouble(args[1]);
        t_f = Integer.parseInt(args[2]);
        simulationFilename = args[3];

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
        IntegrationScheme beemanScheme = new Beeman(mass, k, gamma, new Person(r_0, v_0));
        Simulation simulations = new SimulationImpl(beemanScheme, simulationFilename, conds, gap, dt, t_f);

        simulations.initializeSimulation();
        while (!simulations.isFinished()) {
            simulations.nextIteration();
            try {
                simulations.printIteration();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        simulations.terminate();

    }
}
