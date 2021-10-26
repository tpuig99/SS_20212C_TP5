package oscillators;

import models.Oscillator;

public class OriginalVerlet implements IntegrationScheme {
    private final double mass;
    private final double k;
    private final double gamma;
    private Oscillator prevOscillator = null;
    private Oscillator currOscillator;

    public OriginalVerlet(double mass, double k, double gamma, Oscillator currOscillator) {
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

    @Override
    public double calculatePosition(double dt) {

        if (prevOscillator == null) {
            generatePrevOscillator(dt);
        }

        return 2 * currOscillator.getR() - prevOscillator.getR()
                + calculateAcceleration(currOscillator) * Math.pow(dt, 2);
    }

    @Override
    public double calculateVelocity(double dt) {
        if (prevOscillator == null) {
            generatePrevOscillator(dt);
        }

        return ( calculatePosition(2*dt) - prevOscillator.getR()) / (2 * dt);
    }

    @Override
    public void updateOscillator(double newPosition, double newVelocity) {
        this.prevOscillator = new Oscillator(currOscillator.getR(), currOscillator.getV());
        this.currOscillator = new Oscillator(newPosition, newVelocity);
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
