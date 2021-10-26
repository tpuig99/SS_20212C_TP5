package simulation;

import oscillators.AnalyticSolution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AnalyticSimulation implements Simulation {

    private final AnalyticSolution scheme;
    private final String simulationFilename;
    private double t;
    private final double dt;
    private final double t_f;

    private FileWriter fileWriter;
    private PrintWriter printWriter;

    public AnalyticSimulation(AnalyticSolution scheme, String simulationFilename, double dt, double t_f) {
        this.scheme = scheme;
        this.simulationFilename = simulationFilename;
        this.t = 0;
        this.dt = dt;
        this.t_f = t_f;
    }

    @Override
    public void initializeSimulation() throws IOException {
        fileWriter = new FileWriter(simulationFilename, false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));
        printWriter.println(t);
        printWriter.println(scheme.calculatePosition(0) + " 0");
    }

    @Override
    public void nextIteration() {
        t += dt;
    }

    @Override
    public void printIteration() {
        printWriter.println((float) t);
        printWriter.println(scheme.calculatePosition(t) + " 0");
    }

    @Override
    public boolean isFinished() {
        return t_f < t;
    }

    @Override
    public void terminate() throws IOException {
        printWriter.close();
        fileWriter.close();
    }

    @Override
    public double calculateECM() {
        return 0;
    }
}
