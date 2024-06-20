package PSO;

import java.util.Random;

/**
 * Represents a particle from the Particle Swarm Optimization algorithm.
 */
class Particle {

    private int dimensionNum;
    private Vector position;        // Current position.
    private Vector velocity;
    private Vector bestPosition;    // Personal best solution.
    private double bestEval;        // Personal best value.
    private FunctionType function;  // The evaluation function to use.

    /**
     * Construct a Particle with a random starting position.
     * @param beginRange    the minimum xyz values of the position (inclusive)
     * @param endRange      the maximum xyz values of the position (exclusive)
     */
    Particle (FunctionType function, int dimensionNum, int beginRange, int endRange) {
        if (beginRange >= endRange) {
            throw new IllegalArgumentException("Begin range must be less than end range.");
        }
        this.function = function;
        this.dimensionNum = dimensionNum;
        position = new Vector(dimensionNum);
        velocity = new Vector(dimensionNum);
        setRandomPosition(beginRange, endRange);
        bestPosition = velocity.clone();
        bestEval = eval();
    }

    /**
     * The evaluation of the current position.
     * @return      the evaluation
     */
    private double eval () { //TODO this is hardcoded right now, can generalize later
        
        double[] positionArr = position.getDimensions();

        if (function == FunctionType.FunctionA) {
            return Function.functionA(positionArr[0]);
        } else if (function == FunctionType.Ackleys) {
            return Function.ackleysFunction(positionArr[0], positionArr[1]);
        } else if (function == FunctionType.Booths) {
            return Function.boothsFunction(positionArr[0], positionArr[1]);
        } else {
            return Function.threeHumpCamelFunction(positionArr[0], positionArr[1]);
        }
    }

    private void setRandomPosition (int beginRange, int endRange) {
        double[] placeholderArr = new double[dimensionNum];

        for(int i = 0; i < dimensionNum; i++) {
            placeholderArr[i] = rand(beginRange, endRange);
        }


        position.set(placeholderArr);
    }

    /**
     * Generate a random number between a certain range.
     * @param beginRange    the minimum value (inclusive)
     * @param endRange      the maximum value (exclusive)
     * @return              the randomly generated value
     */
    private static int rand (int beginRange, int endRange) {
        Random r = new java.util.Random();
        return r.nextInt(endRange - beginRange) + beginRange;
    }

    /**
     * Update the personal best if the current evaluation is better.
     */
    void updatePersonalBest () {
        double eval = eval();
        if (eval < bestEval) {
            bestPosition = position.clone();
            bestEval = eval;
        }
    }

    /**
     * Get a copy of the position of the particle.
     * @return  the x position
     */
    Vector getPosition () {
        return position.clone();
    }

    /**
     * Get a copy of the velocity of the particle.
     * @return  the velocity
     */
    Vector getVelocity () {
        return velocity.clone();
    }

    /**
     * Get a copy of the personal best solution.
     * @return  the best position
     */
    Vector getBestPosition() {
        return bestPosition.clone();
    }

    /**
     * Get the value of the personal best solution.
     * @return  the evaluation
     */
    double getBestEval () {
        return bestEval;
    }

    /**
     * Update the position of a particle by adding its velocity to its position.
     */
    void updatePosition () {
        this.position.add(velocity);
    }

    /**
     * Set the velocity of the particle.
     * @param velocity  the new velocity
     */
    void setVelocity (Vector velocity) {
        this.velocity = velocity.clone();
    }

    public enum FunctionType {
        FunctionA,
        Ackleys,
        Booths,
        ThreeHumpCamel
    }

}
