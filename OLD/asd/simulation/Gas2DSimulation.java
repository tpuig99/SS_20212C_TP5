package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.InitialConditions;
import io.Output;
import models.Direction;
import models.Event;
import models.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gas2DSimulation implements Simulation {

    /* General values */
    private final double gap;
    private final String simulationFilename;
    private final long N;
    private final double Lx;
    private final double Ly;
    private final double rmin;
    private final double rmax;
    private final double v;
    private final double gapSize;
    private final double wallSize;

    /* Iteration values */
    private long iteration;
    private double time;
    private List<Particle>  particles;
    private List<Particle>  prevParticles;
    private List<Event> eventsList;
    private CollisionResolver collisionResolver;
    double[][] collisionMatrix;
    private String minCol1, minCol2;
    private double lastPercentage = 1;
    private boolean flag;
    private long cutCriteria;
    private int lapse;

    private FileWriter fileWriter;
    private  PrintWriter printWriter;

    /* Constants */
    private static final double ERROR = 0.001;
    private static final double EQUILIBRIUM = 0.55;
    private static final double EPSILON = 10E-6;

    public Gas2DSimulation(InitialConditions initialConditions, double gap, int lapse, String simulationFilename) {
        this.gap = gap;
        this.simulationFilename = simulationFilename;
        this.lapse = lapse;
        N = initialConditions.getN();
        Lx = initialConditions.getLx();
        Ly = initialConditions.getLy();
        rmin = initialConditions.getRmin();
        rmax = initialConditions.getRmax();
        v = initialConditions.getV();
        particles = new ArrayList<>(initialConditions.getParticles());
        wallSize = ((1-gap) / 2) * Ly;
        gapSize = gap * Ly;
    }

    private double calculatePercentage() {
        long leftParticles = 0;
        for (Particle p : particles) {
            if (p.getX() < Lx/2) {
                leftParticles++;
            }
        }
        return (double) leftParticles / particles.size();
    }

    private void updateMatrix(double minTc, String firstCollision, String secondCollision) {
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size() + 6; j++) {
                if (String.valueOf(i).equals(firstCollision) || String.valueOf(j).equals(firstCollision)
                || String.valueOf(i).equals(secondCollision) || ( String.valueOf(j).equals(secondCollision)
                && !Direction.isValue(secondCollision))) {
                    collisionMatrix[i][j] = -1;
                } else if (collisionMatrix[i][j] != -1) {
                    collisionMatrix[i][j] -= minTc;
                }
            }
        }
    }

    private double checkCollisionInX(Particle p1, int i, int pLen, double minTc) {
        int xLen = pLen + ((p1.getVx() > 0)? 0 : 1);
        double tc = collisionMatrix[i][xLen];
        if (Double.compare(tc, -1) == 0) {
            tc = ((p1.getVx() > 0) ? Lx - p1.getR() - p1.getX() : p1.getR() - p1.getX()) / p1.getVx();
            collisionMatrix[i][xLen] = tc;
        }
        if (tc > 0 && tc < minTc) {
            minTc = tc;
            minCol1 = String.valueOf(i);
            minCol2 = ((p1.getVx() > 0)? Direction.R.name() : Direction.L.name());
        }
        return minTc;
    }

    private double checkCollisionInY(Particle p1, int i, int pLen, double minTc) {
        int yLen = pLen + ((p1.getVy() > 0)? 2 : 3);
        double tc = collisionMatrix[i][yLen];
        if (Double.compare(tc, -1) == 0) {
            tc = ((p1.getVy() > 0) ? Ly - p1.getR() - p1.getY() : p1.getR() - p1.getY()) / p1.getVy();
            collisionMatrix[i][yLen] = tc;
        }
        if (tc > 0 && tc < minTc) {
            minTc = tc;
            minCol1 = String.valueOf(i);
            minCol2 = ((p1.getVy() > 0)? Direction.T.name() : Direction.B.name());
        }
        return minTc;
    }

    private double checkCollisionWithM(Particle p1, int i, int pLen, double minTc) {
        double tc = collisionMatrix[i][pLen + 4];

        if (Double.compare(tc, -1) == 0) {
            if (p1.getX() + p1.getR() < Lx / 2 - EPSILON) {
                tc = (Lx / 2 - p1.getR() - p1.getX()) / p1.getVx();
            } else if (p1.getX() - p1.getR() > Lx / 2 + EPSILON) {
                tc = (Lx / 2 + p1.getR() - p1.getX()) / p1.getVx();
            }
            double yCol = p1.getY() + p1.getVy() * tc;
            if ((yCol - p1.getR()) < wallSize || (yCol + p1.getR()) > wallSize + gapSize) {
                collisionMatrix[i][pLen + 4] = tc;
            } else {
                tc = -1;
            }
        }
        if (tc > 0 && tc < minTc) {
            minTc = tc;
            minCol1 = String.valueOf(i);
            minCol2 = Direction.M.name();
        }
        return minTc;
    }

    private double checkCollisionWithVertex(Particle p1, int i, int pLen, double minTc) {
        double tc = collisionMatrix[i][pLen + 5];

        if (Double.compare(tc, -1) == 0) {
            double tcb = collisionResolver.calculateCollisionWithBottomVertex(p1);
            double tct = collisionResolver.calculateCollisionWithTopVertex(p1);

            if (tct > -1) {
                tc = tct;
            } else if (tcb > -1) {
                tc = tcb;
            }
            if (Double.compare(tc, -1) != 0) {
                collisionMatrix[i][pLen + 5] = tc;
            }
        }
        if (tc > 0 && tc < minTc) {
            minTc = tc;
            minCol1 = String.valueOf(i);
            minCol2 = Direction.V.name();
        }
        return minTc;
    }

    private double calculateNextCollision() {
        double minTc = Double.MAX_VALUE, tc;
        minCol1 = "-1";
        minCol2 = "-1";

        int pLen = particles.size();

        for (int i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);

            /* Check Left/Right Collisions */
            minTc = checkCollisionInX(p1, i, pLen, minTc);
            /* Check Top/Bottom Collisions */
            minTc = checkCollisionInY(p1, i, pLen, minTc);
            /* Check Collision With Middle Bar */
            minTc = checkCollisionWithM(p1, i, pLen, minTc);
            /* Check Collision With Vertex */
            minTc = checkCollisionWithVertex(p1, i, pLen, minTc);

            for (int j = i + 1; j < pLen; j++) {
                tc = collisionMatrix[i][j];
                if (Double.compare(tc, -1) == 0) {
                    tc = collisionResolver.calculateParticleCollision(p1, particles.get(j));
                }
                if (tc > 0 && tc < minTc) {
                    minTc = tc;
                    minCol1 = String.valueOf(i);
                    minCol2 = String.valueOf(j);
                }
            }
        }

        return minTc;
    }

    public void moveAndUpdateParticles(double minTc, String minCol1, String minCol2) {
        Particle collider1Before = null, collider1After = null, collider2Before = null, collider2After = null;
        int index = 0;
        for (Particle p : prevParticles) {
            Particle newP = p.move(minTc);

            if (newP.getId().equals(minCol1) && Direction.isValue(minCol2)) {
                collider1After = newP.generateCopy();
                if (Direction.isValue(minCol2) && Direction.valueOf(minCol2) == Direction.V) {
                    if (collider1After.getY() > Ly/2) {
                        collisionResolver.updateVelocityInCollisionWithVertex(collider1After, wallSize + gapSize);
                    } else {
                        collisionResolver.updateVelocityInCollisionWithVertex(collider1After, wallSize);
                    }
                } else {
                    collisionResolver.updateVelocityInCollisionWithRoof(collider1After, Direction.valueOf(minCol2));
                }
            }

            else if (newP.getId().equals(minCol1)) {
                collider1Before = newP.generateCopy();
            }
            else if (newP.getId().equals(minCol2)) {
                collider2Before = newP.generateCopy();
            }

            particles.set(index++, newP);
        }

        if (!Direction.isValue(minCol2)) {
            collider1After = collider1Before.generateCopy();
            collisionResolver.updateVelocityInCollisionWithParticle(collider1After, collider2Before, 1);
            collider2After = collider2Before.generateCopy();
            collisionResolver.updateVelocityInCollisionWithParticle(collider2After, collider1Before, -1);
        }

        particles.set(Integer.parseInt(minCol1), collider1After);

        if (!Direction.isValue(minCol2)) {
            particles.set(Integer.parseInt(minCol2), collider2After);
        }
    }


    @Override
    public void initializeSimulation() throws IOException {
        iteration = 0;
        time = 0;
        flag = false;
        collisionResolver = new CollisionResolver(Lx, gapSize, wallSize);
        collisionMatrix = new double[particles.size() + 6][particles.size() + 6];

        for (int i = 0; i < collisionMatrix.length; i++) {
            for (int j = 0; j < collisionMatrix.length; j++) {
                collisionMatrix[i][j] = -1;
            }
        }

        eventsList = new ArrayList<>();
        eventsList.add(new Event(time, new ArrayList<>(particles), new String[] { "-1", "-1" }));

        prevParticles = new ArrayList<>();

        fileWriter = new FileWriter(simulationFilename, true);
        printWriter = new PrintWriter(new BufferedWriter(fileWriter));
    }

    @Override
    public void nextIteration() {
        prevParticles = new ArrayList<>(particles);

        // prevParticles = particles.clone();
        double minTc = calculateNextCollision();
        moveAndUpdateParticles(minTc, minCol1, minCol2);
        updateMatrix(minTc, minCol1, minCol2);
        time += minTc;
        if ((iteration + 1) % lapse == 0) {
            eventsList.add(new Event(time, new ArrayList<>(particles), new String[] {minCol1, minCol2}));
        }
        iteration++;
    }

    @Override
    public void printIteration() {
        if (iteration == 1) {
            String header = "N: " + N + ' ' +
                    "Lx: " + Lx + ' ' +
                    "Ly: " + Ly + ' ' +
                    "gap: " + gap + ' ' +
                    "rmin: " + rmin + ' ' +
                    "rmax: " + rmax + ' ' +
                    "v: " + v + ' ';
            printWriter.println(header);
        }
        if (Math.abs(lastPercentage - calculatePercentage()) > 0.01) {
            lastPercentage = calculatePercentage();
            System.out.printf("Percentage: %.3f\n", lastPercentage);
        }
        printWriter.println(time);
        printWriter.println(Arrays.toString(new String[]{minCol1, minCol2}));
        for (Particle p : prevParticles) {
            printWriter.println(p.toString());
        }
    }

    @Override
    public boolean isFinished() {

        if (flag) {
            cutCriteria--;
            return cutCriteria <= 0;
        }

        boolean generalRule = calculatePercentage() <= EQUILIBRIUM + ERROR;

        if (generalRule && !flag) {
            flag = true;
            if (iteration < 10) {
                cutCriteria = 10;
            } else {
                cutCriteria = (long) Math.floor(iteration * 0.1);
            }
        }

        return false;
    }

    @Override
    public void terminate() throws IOException {
        try {
            final Output output = new Output(N, Lx, Ly, rmin, rmax, v, gap, eventsList.size(), new ArrayList<>(eventsList));
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Paths.get(simulationFilename.replace(".txt", "_light.json")).toFile(), output);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        printWriter.close();
        fileWriter.close();
    }
}
