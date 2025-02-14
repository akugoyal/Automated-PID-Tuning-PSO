package frc.robot.PSO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import frc.robot.RobotMap;
import frc.robot.PSO.Particle.FunctionType;
import frc.robot.util.Telemetry;

/**
 * Represents a swarm of particles from the Particle Swarm Optimization algorithm.
 */
public class Swarm {

    private int numOfParticles, epochs, dimensionNum;
    private double inertia, cognitiveComponent, socialComponent;
    private Vector bestPosition;
    private double bestEval;
    private Particle[] particles;
    private FunctionType function; // The function to search.
    public static final double DEFAULT_INERTIA = 0.729844;
    public static final double DEFAULT_COGNITIVE = 1.496180; // Cognitive component.
    public static final double DEFAULT_SOCIAL = 1.496180; // Social component.

    public static int currentEpoch;
    public static int currentParticle;

    /**
     * When Particles are created they are given a random position.
     * The random position is selected from a specified range.
     * If the begin range is 0 and the end range is 10 then the
     * value will be between 0 (inclusive) and 10 (exclusive).
     */
    private double beginRange, endRange;
    private static final double DEFAULT_BEGIN_RANGE = 0; //TODO
    private static final double DEFAULT_END_RANGE = 0.05;

    /**
     * Construct the Swarm with default values.
     * @param particles     the number of particles to create
     * @param epochs        the number of generations
     */
    public Swarm (FunctionType function, int dimensionNum, int particles, int epochs) {
        this(function, dimensionNum, particles, epochs, DEFAULT_INERTIA, DEFAULT_COGNITIVE, DEFAULT_SOCIAL);
    }

    /**
     * Construct the Swarm with custom values.
     * @param particles     the number of particles to create
     * @param epochs        the number of generations
     * @param inertia       the particles resistance to change
     * @param cognitive     the cognitive component or introversion of the particle
     * @param social        the social component or extroversion of the particle
     */
    public Swarm (FunctionType function, int dimensionNum, int particles, int epochs, double inertia, double cognitive, double social) {
        this.numOfParticles = particles;
        this.dimensionNum = dimensionNum;
        this.epochs = epochs;
        this.inertia = inertia;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.function = function;
        double infinity = Double.POSITIVE_INFINITY;

        double[] placeholderArr = new double[dimensionNum];

        for (int i = 0; i < dimensionNum; i++) { //TODO theres probably a better way to do this
            placeholderArr[i] = infinity;
        }
        bestPosition = new Vector(placeholderArr);
        bestEval = Double.POSITIVE_INFINITY;
        beginRange = DEFAULT_BEGIN_RANGE;
        endRange = DEFAULT_END_RANGE;
    }

    /**
     * Execute the algorithm.
     */
    public void run () {

        particles = new Particle[RobotMap.PSO.NUM_PARTICLES];
        RobotMap.PSO.particles = particles;

        if(RobotMap.Arm.LOAD_SWARM) {
            initializeFromFile();
        } else {
            initialize();
        }

        double oldEval = bestEval;
        System.out.println("--------------------------EXECUTING-------------------------");
        System.out.println("Global Best Evaluation (Epoch " + 0 + "):\t"  + bestEval);

        for (int i = 0; i < epochs; i++) {
            Telemetry.putNumber("pivot", "epochs", i);
            currentEpoch = i + 1;

            if (bestEval < oldEval) {
                System.out.println("Global Best Evaluation (Epoch " + (i + 1) + "):\t" + bestEval);
                oldEval = bestEval;
            }

            for (int j = 0; j < particles.length; j++) {

                if (RobotMap.Arm.SAVE_SWARM) RobotMap.Arm.currentSaveFile_Log = RobotMap.Arm.SAVE_FILE_HEADER + (i + 1) + "_LOG.txt";

                Telemetry.putNumber("pivot", "bPos0", bestPosition.getDimensions()[0]);
                Telemetry.putNumber("pivot", "bPos1", bestPosition.getDimensions()[1]);
                Telemetry.putNumber("pivot", "bPos2", bestPosition.getDimensions()[2]);

                currentParticle = j + 1;

                Particle p = particles[j];
                Telemetry.putNumber("pivot", "particle number", j);
                p.updatePersonalBest();
                updateGlobalBest(p);
                
                if (RobotMap.Arm.SAVE_SWARM) {
                    String fName = RobotMap.Arm.SAVE_FILE_HEADER + (i + 1) + ".txt";
                    
                    saveToFile(particles, fName);
                }

            }

            for (Particle p : particles) {
                updateVelocity(p);
                p.updatePosition();
            }
        }

        System.out.println("---------------------------RESULT---------------------------");
        System.out.println(bestPosition.toString());
        System.out.println("Final Best Evaluation: " + bestEval);
        System.out.println("---------------------------COMPLETE-------------------------");
    }

    private void initializeFromFile() { //TODO add config checkers
        Scanner in;
        try {
            in = new Scanner(new File(RobotMap.Arm.LOAD_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        try {
            bestEval = in.nextDouble();
        } catch (InputMismatchException e) {
            bestEval = 0.0; //TODO do smth else here
            e.printStackTrace();
        }

        double[] bPos = new double[dimensionNum];

        for(int j = 0; j < dimensionNum; j++) {
            try {
                bPos[j] = in.nextDouble();
            } catch (InputMismatchException e) {
                bPos[j] = 0.0; //TODO do smth else here
                e.printStackTrace();
            }
        }

        bestPosition = new Vector(bPos);

        for(int i = 0; i < numOfParticles; i++) {

            double[] pos = new double[dimensionNum];

            for(int j = 0; j < dimensionNum; j++) { // current position
                try {
                    pos[j] = in.nextDouble();
                } catch (InputMismatchException e) {
                    pos[j] = Particle.rand(DEFAULT_BEGIN_RANGE, DEFAULT_END_RANGE); //TODO do smth else here
                    e.printStackTrace();
                }
            }

            Vector position = new Vector(pos);

            double[] vel = new double[dimensionNum];

            for(int j = 0; j < dimensionNum; j++) { // current velocity
                try {
                    vel[j] = in.nextDouble();
                } catch (InputMismatchException e) {
                    vel[j] = Particle.rand(DEFAULT_BEGIN_RANGE, DEFAULT_END_RANGE); //TODO do smth else here
                    e.printStackTrace();
                }
            }

            Vector velocity = new Vector(vel);

            double[] bIndivPos = new double[dimensionNum];

            for(int j = 0; j < dimensionNum; j++) { // best individual position
                try {
                    bIndivPos[j] = in.nextDouble();
                } catch (InputMismatchException e) {
                    bIndivPos[j] = 0.0; //TODO do smth else here
                    e.printStackTrace();
                }
            }

            Vector bestIndividualPosition = new Vector(bIndivPos);

            double bestIndivEval;
            try {
                bestIndivEval = in.nextDouble();
            } catch (InputMismatchException e) {
                bestIndivEval = 0.0; //TODO do smth else here
                e.printStackTrace();
            }

            particles[i] = new Particle(function, dimensionNum, position, velocity, bestIndividualPosition, bestIndivEval);

        }
    }

    /**
     * Create a set of particles, each with random starting positions.
     * @return  an array of particles
     */
    private void initialize () {
        if (RobotMap.Arm.SAVE_SWARM) {
                RobotMap.Arm.currentSaveFile_Log = RobotMap.Arm.SAVE_FILE_HEADER + "0" + "_LOG.txt";
            }
        for (int i = 0; i < numOfParticles; i++) {
            Telemetry.putNumber("pivot", "particle number", i);
            Telemetry.putNumber("pivot", "bPos0", bestPosition.getDimensions()[0]);
            Telemetry.putNumber("pivot", "bPos1", bestPosition.getDimensions()[1]);
            Telemetry.putNumber("pivot", "bPos2", bestPosition.getDimensions()[2]);
            Particle particle = new Particle(function, dimensionNum, beginRange, endRange);
            particles[i] = particle;
            if (RobotMap.Arm.SAVE_SWARM) {
                saveToFile(particles, RobotMap.Arm.SAVE_FILE_HEADER + "0.txt");
            }
            updateGlobalBest(particle);
        }
    }

    private void saveToFile(Particle[] particleArr, String fName) {
        Telemetry.putString("pivot", "fileName", fName);
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(fName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        try {
            out.write(bestEval + "\n");
        } catch (IOException e) {
            
            e.printStackTrace();
            throw new RuntimeException("Saving to file has failed");
        }

        double[] bPos = bestPosition.getDimensions();

        for(int j = 0; j < dimensionNum; j++) {
            try {
                out.write(bPos[j] + "\n\n");
            } catch (IOException e) {
                
                e.printStackTrace();
                throw new RuntimeException("Saving to file has failed");
            }
        }


        for(int i = 0; i < numOfParticles; i++) {
            if (particleArr[i] == null) {
                continue;
            }
            double[] pos = particleArr[i].getPosition().getDimensions();

            for(int j = 0; j < dimensionNum; j++) {
                try {
                    out.write(pos[j]+ "\n");
                } catch (IOException e) {
                    // out.writeDouble(0.0);
                    e.printStackTrace();
                    throw new RuntimeException("Saving to file has failed");
                }
            }


            double[] vel = particleArr[i].getVelocity().getDimensions();

            for(int j = 0; j < dimensionNum; j++) {
                try {
                    out.write(vel[j] + "\n");
                } catch (IOException e) {
                    // out.writeDouble(0.0);
                    e.printStackTrace();
                    throw new RuntimeException("Saving to file has failed");
                }
            }

            double[] bIndivPos = particleArr[i].getBestPosition().getDimensions();

            for(int j = 0; j < dimensionNum; j++) {
                try {
                    out.write(bIndivPos[j] + "\n");
                } catch (IOException e) {
                    // out.writeDouble(0.0);
                    e.printStackTrace();
                    throw new RuntimeException("Saving to file has failed");
                }
            }

            try {
                out.write(particleArr[i].getBestEval() + "\n\n");
            } catch (IOException e) {
                
                e.printStackTrace();
                throw new RuntimeException("Saving to file has failed");
            }

        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("BufferedWriter failed to close");
        }
    }

    /**
     * Update the global best solution if a the specified particle has
     * a better solution
     * @param particle  the particle to analyze
     */
    private void updateGlobalBest (Particle particle) {
        if (particle.getBestEval() < bestEval) {
            bestPosition = particle.getBestPosition();
            bestEval = particle.getBestEval();
        }
    }

    /**
     * Update the velocity of a particle using the velocity update formula
     * @param particle  the particle to update
     */
    private void updateVelocity (Particle particle) {
        Vector oldVelocity = particle.getVelocity();
        Vector pBest = particle.getBestPosition();
        Vector gBest = bestPosition.clone();
        Vector pos = particle.getPosition();

        double r1 = Math.random();
        double r2 = Math.random();

        // The first product of the formula.
        Vector newVelocity = oldVelocity.clone();
        newVelocity.mul(inertia);

        // The second product of the formula.
        pBest.sub(pos);
        pBest.mul(cognitiveComponent);
        pBest.mul(r1);
        newVelocity.add(pBest);

        // The third product of the formula.
        gBest.sub(pos);
        gBest.mul(socialComponent);
        gBest.mul(r2);
        newVelocity.add(gBest);

        particle.setVelocity(newVelocity);
    }

}
