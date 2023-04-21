package net.sf.janos.model;

/**
 * An immutable data class containing a left and right line level.
 * @author David Wheeler
 *
 */
public class LineLevel {

    private int left;

    private int right;

    public LineLevel(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
