package oscillators;

import models.Oscillator;

public class Beeman implements IntegrationScheme {

    private final double mass;
    private final double k;
    private final double gamma;
    private Oscillator prevOscillator = null;
    private Oscillator currOscillator;

    public Beeman(double mass, double k, double gamma, Oscillator currOscillator) {
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.currOscillator = currOscillator;
    }

    private void generatePrevOscillator(double dt) {
        this.prevOscillator = new Oscillator(eulerPosition(-dt), eulerVelocity(-dt));
    }

    private double eulerPosition(double dt) {
        return currOscillator.getR() + currOscillator.getV() * dt +
                calculateAcceleration(currOscillator) * Math.pow(dt, 2) / 2;
    }

    private double eulerVelocity(double dt) {
        return currOscillator.getV() + calculateAcceleration(currOscillator) * dt;
    }

    private double calculateAcceleration(Oscillator oscillator) {
        return calculateForce(oscillator) / mass;
    }

    private double calculateForce(Oscillator oscillator) {
        return -k * oscillator.getR() - gamma * oscillator.getV();
    }

    private double predictVelocity(double dt) {
        double aCurr = calculateAcceleration(currOscillator);
        double aPrev = calculateAcceleration(prevOscillator);

        return currOscillator.getV() + 1.5 * aCurr * dt - 0.5 * aPrev * dt;
    }

    @Override
    public double calculatePosition(double dt) {

        if (prevOscillator == null) {
            generatePrevOscillator(dt);
        }

        double aCurr = calculateAcceleration(currOscillator);
        double aPrev = calculateAcceleration(prevOscillator);

        return currOscillator.getR()
                + currOscillator.getV() * dt
                + (1.0 / 6) * (4 * aCurr - aPrev) * Math.pow(dt, 2);
    }

    @Override
    public double calculateVelocity(double dt) {

        if (prevOscillator == null) {
            generatePrevOscillator(dt);
        }

        double aCurr = calculateAcceleration(currOscillator);
        double aPrev = calculateAcceleration(prevOscillator);

        Oscillator predictOscillator = new Oscillator(calculatePosition(dt), predictVelocity(dt));

        double aFut = calculateAcceleration(predictOscillator);

        return currOscillator.getV()
                + (1.0 / 6) * (2 * aFut + 5 * aCurr - aPrev) * dt;
    }

    @Override
    public void updateOscillator(double newPosition, double newVelocity) {
        prevOscillator = new Oscillator(currOscillator.getR(), currOscillator.getV());
        currOscillator = new Oscillator(newPosition, newVelocity);
    }

    @Override
    public Oscillator getOscillator() {
        return currOscillator;
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
