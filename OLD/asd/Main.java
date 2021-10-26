import com.fasterxml.jackson.databind.ObjectMapper;
import io.InitialConditions;
import simulation.Gas2DSimulation;
import simulation.Simulation;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    private static String path;
    private static double gap;
    private static int lapse;
    private static String simulationFilename;

    public static void main(String[] args) throws IOException {

        if (args.length != 4) {
            System.err.println("Wrong amount of arguments");
            return;
        }

        InitialConditions conditions = parseArguments(args);

        if (conditions != null) {
            Simulation simulation = new Gas2DSimulation(conditions, gap, lapse, simulationFilename);
            simulation.initializeSimulation();

            long startTime = System.currentTimeMillis();

            while (!simulation.isFinished()) {
                simulation.nextIteration();
                try {
                    simulation.printIteration();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            simulation.terminate();

            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;
            long secondsDisplay = elapsedSeconds % 60;
            long elapsedMinutes = elapsedSeconds / 60;

            System.out.printf("Total time: %d:%d\n", elapsedMinutes, secondsDisplay);
        }
    }

    public static InitialConditions parseArguments(String[] args) {

        path = args[0];
        gap = Double.parseDouble(args[1]);
        lapse = Integer.parseInt(args[2]);
        simulationFilename = args[3];

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(path).toFile(), InitialConditions.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
