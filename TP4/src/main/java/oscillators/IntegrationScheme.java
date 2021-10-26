package oscillators;

import models.Oscillator;

public interface IntegrationScheme {
    double calculatePosition(double dt);
    double calculateVelocity(double dt);
    void updateOscillator(double newPosition, double newVelocity);
    Oscillator getOscillator();
    double getMass();
    double getK();
    double getGamma();
}
