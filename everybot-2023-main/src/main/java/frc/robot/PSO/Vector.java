package PSO;

/**
 * Can represent a position as well as a velocity.
 */
class Vector {

    private double[] dimensions;
    private int dimensionNumber;
    private double limit = Double.MAX_VALUE;

    Vector () {
        this(new double[] {0.0, 0.0, 0.0});
    }

    Vector (int dimensionNumber) {
        this.dimensionNumber = dimensionNumber;

        dimensions = new double[dimensionNumber];

        for (int i = 0; i < dimensionNumber; i++) {
            this.dimensions[i] = 0.0;
        }
    }

    Vector (double[] dimensions) {

        dimensionNumber = dimensions.length;

        this.dimensions = new double[dimensionNumber];
        
        for (int i = 0; i < dimensionNumber; i++) {
            this.dimensions[i] = dimensions[i];
        }
        
    }

    double[] getDimensions() { //TODO check if this needs to be deep copied
        return dimensions;
    }

    int getDimensionNumber() {
        return dimensionNumber;
    }

    void set (double[] dimensions) {

        if (this.dimensions.length != dimensions.length) {
            throw new IllegalArgumentException("Error: dimension arrays do not have the same number of dimeonsions as vector");
        }

        this.dimensions = dimensions;
    }


    void add (Vector v) {

        if (dimensions.length != v.getDimensionNumber()) {
            throw new IllegalArgumentException("Error: dimension arrays do not have the same number of dimeonsions as vector");
        }

        for (int i = 0; i < dimensionNumber; i++) {
            dimensions[i] += v.getDimensions()[i];
        }
        
        limit();
    }

    void sub (Vector v) {

        if (this.dimensions.length != v.getDimensionNumber()) {
            throw new IllegalArgumentException("Error: dimension arrays do not have the same number of dimeonsions as vector");
        }

        for (int i = 0; i < dimensionNumber; i++) {
            dimensions[i] -= v.getDimensions()[i];
        }

        limit();
    }

    void mul (double s) {

        for (int i = 0; i < dimensionNumber; i++) {
            dimensions[i] *= s;
        }

        limit();
    }

    void div (double s) {
        
        for (int i = 0; i < dimensionNumber; i++) {
            dimensions[i] /= s;
        }
        
        limit();
    }

    void normalize () {
        double m = mag();
        
        if (m > 0) div(m);
    }

    private double mag () {
        
        double sum = 0.0;

        for (int i = 0; i < dimensionNumber; i++) {
            sum += dimensions[i] * dimensions[i];
        }

        return Math.sqrt(sum);
    }

    void limit (double l) {
        limit = l;
        limit();
    }

    private void limit () {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            
            for (int i = 0; i < dimensionNumber; i++) {
                dimensions[i] /= ratio;
            }
        }
    }

    public Vector clone () {
        return new Vector(dimensions);
    }

    public String toString () {
        
        String str = "(";

        for (int i = 0; i < dimensionNumber - 1; i++) {
            str += dimensions[i] + ", ";
        }

        str += dimensions[dimensionNumber - 1] + ")";

        return str;
    }

}
