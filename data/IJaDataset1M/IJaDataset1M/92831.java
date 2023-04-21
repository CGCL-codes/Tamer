package net.sf.cannonfodder.model;

import net.sf.cannonfodder.player.NetworkPlayer;

/**
 * This class represents cannonballs. It has methods to move
 * the cannonballs and check where they are at and find out
 * how long a shot they are on.
 * Created 3-17-2001
 * Revision History:
 * 3-22-2001 added shotDistance, currentXand currentY methods
 *
 * @author Zachary Kuhn
 */
public class CannonBall {

    protected Cannon firedFrom = null;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    private double currentX;

    private double currentY;

    protected double distanceMoved = 0.01;

    protected double moveRate = .07;

    private boolean done = false;

    public CannonBall(Cannon cannon, int endX, int endY) {
        if (cannon.getOwner() instanceof NetworkPlayer) {
            System.out.println("cannon X = " + cannon.getX() + "cannon Y = " + cannon.getY());
        }
        this.firedFrom = cannon;
        this.setStartX(cannon.getX());
        this.setStartY(cannon.getY());
        this.setEndX(endX);
        this.setEndY(endY);
    }

    public CannonBall(int startX, int startY, int endX, int endY, double currentX, double currentY) {
        this.setCurrentX(currentX);
        this.setCurrentY(currentY);
        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(endX);
        this.setEndY(endY);
    }

    public void update() {
        distanceMoved += moveRate;
        updateX();
        updateY();
        if (landed()) {
            firedFrom.setFireable(true);
        }
        return;
    }

    /**
     * This method returns the current position of the cannon ball,
     * and updates it's last position
     */
    public void updateX() {
        setCurrentX((distanceMoved / shotDistance()) * (getEndX() - getStartX()) + getStartX());
    }

    public void updateY() {
        setCurrentY((distanceMoved / shotDistance()) * (getEndY() - getStartY()) + getStartY());
    }

    public double currentX() {
        return getCurrentX();
    }

    public double currentY() {
        return getCurrentY();
    }

    public double shotDistance() {
        return Math.sqrt(Math.pow((double) getEndX() - (double) getStartX(), 2.0) + Math.pow((double) getEndY() - (double) getStartY(), 2.0));
    }

    /**
     * This function checks to see if the cannonball has traveled the amount of distance
     * to have reached it's destination.
     */
    public boolean landed() {
        if (distanceMoved >= shotDistance()) {
            return true;
        }
        return false;
    }

    public void setDone() {
        done = true;
    }

    public boolean done() {
        return done;
    }

    public Cannon firedFrom() {
        return firedFrom;
    }

    public String toString() {
        String returnString = firedFrom + "endX = " + getEndX() + "\n" + "endY = " + getEndY() + "\n" + "distanceMoved = " + distanceMoved + "\n" + "moveRate = " + moveRate + "\n" + "shotDistance = " + shotDistance() + "\n";
        return returnString;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }
}
