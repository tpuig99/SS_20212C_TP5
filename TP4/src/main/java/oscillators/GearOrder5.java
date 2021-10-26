package oscillators;

import models.Oscillator;

public class GearOrder5 implements IntegrationScheme {

    private final double mass;
    private final double k;
    private final double gamma;

    private final double[] previous;
    private final double[] currents;
    private final double [] coefficients = new double[] { 3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    public GearOrder5(double mass, double k, double gamma, Oscillator currOscillator) {
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        currents = new double[] {currOscillator.getR(), currOscillator.getV(),0,0,0,0};
        previous = new double[] {currents[0],currents[1], calculateAcceleration(currOscillator.getR(),currOscillator.getV())
                ,0,0,0};
    }

    private double calculateAcceleration(double r, double v) {
        return calculateForce(r,v) / mass;
    }

    private double calculateForce(double r, double v) {
        return -k * r - gamma * v;
    }

    private static int factorial(int n){
        if (n == 0)
            return 1;
        else
            return(n * factorial(n-1));
    }

    @Override
    public double calculatePosition(double dt) {
        double[] currRps = new double[6];

        for (int k = 0 ; k < currents.length; k++) {
            for (int i = 0 ; i < currents.length - k; i++) {
                currRps[k] += previous[i + k] * Math.pow(dt, i) / factorial(i);
            }
        }

        double deltaA = calculateAcceleration(currRps[0], currRps[1]) - currRps[2];
        double deltaR2 = (deltaA * Math.pow(dt, 2)) / 2;

        for (int i = 0 ; i < currents.length ; i++) {
            currents[i] = currRps[i] + coefficients[i] * deltaR2 * factorial(i) / Math.pow(dt, i);
            previous[i] = currents[i];
        }

        return currents[0];
    }

    @Override
    public double calculateVelocity(double dt) {
        return currents[1];
    }

    @Override
    public void updateOscillator(double newPosition, double newVelocity) {
        /* Empty because no update needed */
    }

    @Override
    public Oscillator getOscillator() {
        return new Oscillator(currents[0], currents[1]);
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public double getK() {
        return k;
    }

    @Override
    public double getGamma() {
        return gamma;
    }
}
