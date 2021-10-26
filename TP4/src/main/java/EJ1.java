import models.Oscillator;
import oscillators.*;
import simulation.AnalyticSimulation;
import simulation.OscilatorSimulation;
import simulation.Simulation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EJ1 {

    private static double mass;
    private static double k;
    private static double gamma;
    private static double t_f;
    private static double r_0;
    private static double v_0;
    private static String path;
    private static FileWriter errorFileWriter;
    private static PrintWriter errorPrintWriter;

    public static void main(String[] args) throws IOException {

        if (args.length != 7) {
            System.err.println("Wrong amount of arguments");
            return;
        }

        parseArguments(args);

        errorFileWriter = new FileWriter(path.replace(".txt", "_errors.txt"), false);
        errorPrintWriter = new PrintWriter(new BufferedWriter(errorFileWriter));

        for (int i = -6 ; i < -1 ; i++) {
            double dt = Math.pow(10, i);
            doSimulation(dt, t_f);
        }

        errorPrintWriter.close();
        errorFileWriter.close();

    }

    public static void parseArguments(String[] args) {
        mass = Double.parseDouble(args[0]);
        k = Double.parseDouble(args[1]);
        gamma = Double.parseDouble(args[2]);
        t_f = Double.parseDouble(args[3]);
        r_0 = Double.parseDouble(args[4]);
        double A = Double.parseDouble(args[5]);
        v_0 = - A * gamma / (2 * mass);
        path = args[6];
    }

    public static String fmt (double dt) {
        return String.format("%f", dt).replace(',', '.');
    }

    public static void doSimulation(double currDt, double t_f) throws IOException {
        IntegrationScheme gearScheme = new GearOrder5(mass, k, gamma, new Oscillator(r_0, v_0));
        IntegrationScheme beemanScheme = new Beeman(mass, k, gamma, new Oscillator(r_0, v_0));
        IntegrationScheme verletScheme = new OriginalVerlet(mass, k, gamma, new Oscillator(r_0, v_0));
        AnalyticSolution analyticSolution =  new AnalyticSolution(mass, k, gamma, new Oscillator(r_0,v_0));

        List<Simulation> simulations = new ArrayList<>();

        simulations.add( new OscilatorSimulation(gearScheme, path.replace(".txt", "_gear_" + fmt(currDt) + ".txt"), currDt, t_f ));
        simulations.add( new OscilatorSimulation(beemanScheme, path.replace(".txt", "_beeman_" + fmt(currDt) + ".txt"), currDt, t_f ));
        simulations.add( new OscilatorSimulation(verletScheme, path.replace(".txt", "_verlet_" + fmt(currDt) + ".txt"), currDt, t_f ));
        simulations.add( new AnalyticSimulation(analyticSolution, path.replace(".txt", "_analytic_" + fmt(currDt) + ".txt"), currDt, t_f ));

        /* Change for your own Integration Scheme */

        errorPrintWriter.println(currDt);

        for (Simulation s : simulations) {
            s.initializeSimulation();

            while (!s.isFinished()) {
                s.nextIteration();
                try {
                    s.printIteration();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (! (s instanceof AnalyticSimulation) ) {
                errorPrintWriter.println(s.calculateECM());
            }

            s.terminate();
        }

    }
}
