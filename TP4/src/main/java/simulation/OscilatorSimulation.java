package simulation;

import models.Oscillator;
import oscillators.AnalyticSolution;
import oscillators.IntegrationScheme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class OscilatorSimulation implements Simulation {

    private final IntegrationScheme scheme;
    private final String simulationFilename;
    private double t;
    private final double dt;
    private final double t_f;

    private FileWriter fileWriter;
    private  PrintWriter printWriter;
    private final AnalyticSolution analyticSolution;
    private final List<Double> analytics;
    private final List<Double> values;

    public OscilatorSimulation(IntegrationScheme scheme, String simulationFilename, double dt,double t_f) {
        this.scheme = scheme;
        this.simulationFilename = simulationFilename;
        this.t = 0;
        this.dt = dt;
        this.t_f = t_f;
        this.analyticSolution = new AnalyticSolution(scheme.getMass(), scheme.getK(), scheme.getGamma(), scheme.getOscillator());
        analytics = new ArrayList<>();
        values = new ArrayList<>();
    }

    public double calculateECM() {
        double acum = 0;

        for (int i = 0 ; i < analytics.size() ; i++) {
            acum += Math.pow(analytics.get(i) - values.get(i), 2);
        }

        return (1.0 / analytics.size() ) * acum;
    }

    @Override
    public void initializeSimulation() throws IOException {
        fileWriter = new FileWriter(simulationFilename, false);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));

        analytics.add(analyticSolution.calculatePosition(t));
        values.add(scheme.getOscillator().getR());

        printWriter.println(t);
        printWriter.println(scheme.getOscillator());
    }

    @Override
    public void nextIteration() {
        double r = scheme.calculatePosition(dt);
        double v = scheme.calculateVelocity(dt);
        scheme.updateOscillator(r, v);

        t += dt;

        analytics.add(analyticSolution.calculatePosition(t));
        values.add(scheme.getOscillator().getR());
    }

    @Override
    public void printIteration() {
        printWriter.println((float) t);
        printWriter.println(scheme.getOscillator());
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
}
