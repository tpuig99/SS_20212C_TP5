package simulation;

import java.io.IOException;

public interface Simulation {
    void initializeSimulation() throws IOException;
    void nextIteration();
    void printIteration() throws IOException;
    boolean isFinished();
    void terminate() throws IOException;
}
