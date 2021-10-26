package oscillators;

import models.Person;

public class Beeman implements IntegrationScheme {

    private final double mass;
    private final double k;
    private final double gamma;
    private Person prevPerson = null;
    private Person currPerson;

    public Beeman(double mass, double k, double gamma, Person currPerson) {
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.currPerson = currPerson;
    }

    private void generatePrevOscillator(double dt) {
        this.prevPerson = new Person(eulerPosition(-dt), eulerVelocity(-dt));
    }

    private double eulerPosition(double dt) {
        return currPerson.getR() + currPerson.getV() * dt +
                calculateAcceleration(currPerson) * Math.pow(dt, 2) / 2;
    }

    private double eulerVelocity(double dt) {
        return currPerson.getV() + calculateAcceleration(currPerson) * dt;
    }

    private double calculateAcceleration(Person person) {
        return calculateForce(person) / mass;
    }

    private double calculateForce(Person person) {
        return -k * person.getR() - gamma * person.getV();
    }

    private double predictVelocity(double dt) {
        double aCurr = calculateAcceleration(currPerson);
        double aPrev = calculateAcceleration(prevPerson);

        return currPerson.getV() + 1.5 * aCurr * dt - 0.5 * aPrev * dt;
    }

    @Override
    public double calculatePosition(double dt) {

        if (prevPerson == null) {
            generatePrevOscillator(dt);
        }

        double aCurr = calculateAcceleration(currPerson);
        double aPrev = calculateAcceleration(prevPerson);

        return currPerson.getR()
                + currPerson.getV() * dt
                + (1.0 / 6) * (4 * aCurr - aPrev) * Math.pow(dt, 2);
    }

    @Override
    public double calculateVelocity(double dt) {

        if (prevPerson == null) {
            generatePrevOscillator(dt);
        }

        double aCurr = calculateAcceleration(currPerson);
        double aPrev = calculateAcceleration(prevPerson);

        Person predictPerson = new Person(calculatePosition(dt), predictVelocity(dt));

        double aFut = calculateAcceleration(predictPerson);

        return currPerson.getV()
                + (1.0 / 6) * (2 * aFut + 5 * aCurr - aPrev) * dt;
    }

    @Override
    public void updateOscillator(double newPosition, double newVelocity) {
        prevPerson = new Person(currPerson.getR(), currPerson.getV());
        currPerson = new Person(newPosition, newVelocity);
    }

    @Override
    public Person getOscillator() {
        return currPerson;
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
