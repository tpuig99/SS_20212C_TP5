package oscillators;

import models.Oscillator;

public class AnalyticSolution {

    private final double mass;
    private final double k;
    private final double gamma;
    private final double A;

    public AnalyticSolution (double mass, double k, double gamma, Oscillator oscillator) {
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.A = oscillator.getR();
    }

    public double calculatePosition(double t) {
        double exp = Math.exp(- (gamma / (2 * mass)) * t);
        double sqrt = Math.sqrt( (k / mass) - Math.pow( gamma/(2 * mass ) ,2) );
        double cos = Math.cos( sqrt * t );
        return A * exp * cos;
    }

}
