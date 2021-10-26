package models;

public class Analisys {
    private double minMarsDist;
    private double timeOfArrival;
    private double lunchPercentage;
    private double t_f;

    public Analisys(double minMarsDist, double timeOfArrival, double lunchPercentage, double t_f) {
        this.minMarsDist = minMarsDist;
        this.timeOfArrival = timeOfArrival;
        this.lunchPercentage = lunchPercentage;
        this.t_f = t_f;
    }

    public double getLounchDate(){
        double lounchDateInSegs = lunchPercentage * t_f;
        double lounchDateInDays = lounchDateInSegs / (60 * 60 * 24);
        return lounchDateInDays;
    }

    @Override
    public String toString() {
        return "Analisys{" +
                "minMarsDist=" + minMarsDist +
                ", timeOfArrival=" + timeOfArrival +
                ", lunchPercentage=" + lunchPercentage +
                ", t_f=" + t_f +
                ", getLounchDate=" + getLounchDate() +
                '}';
    }
}
