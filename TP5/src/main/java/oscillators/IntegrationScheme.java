package oscillators;

import models.Person;

public interface IntegrationScheme {
    double calculatePosition(double dt);
    double calculateVelocity(double dt);
    void updateOscillator(double newPosition, double newVelocity);
    Person getOscillator();
    double getMass();
    double getK();
    double getGamma();
}
