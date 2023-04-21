package balls;

import balls.util.*;

/**
 * 
 *
 * @author aricci
 *
 */
public class Boundary {

    private double x0;

    private double y0;

    private double x1;

    private double y1;

    public Boundary(double x0, double y0, double x1, double y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }

    public double getY0() {
        return y0;
    }

    public double getY1() {
        return y1;
    }

    public boolean isOutOfBoundRight(P2d p) {
        return p.x > x1;
    }

    public boolean isOutOfBoundLeft(P2d p) {
        return p.x < x0;
    }
}
