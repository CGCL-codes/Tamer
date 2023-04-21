package com.google.gwt.dev.util.editdistance;

/**
 * A modified version of a string edit distance described by Berghel and
 * Roach that uses only O(d) space and O(n*d) worst-case time, where n is
 * the pattern string length and d is the edit distance computed.
 * We achieve the space reduction by keeping only those sub-computations
 * required to compute edit distance, giving up the ability to
 * reconstruct the edit path.
 */
public class ModifiedBerghelRoachEditDistance implements GeneralEditDistance {

    private static final int[] EMPTY_INT_ARRAY = new int[0];

    /**
   * Creates an instance for computing edit distance from {@code pattern}.
   * @param pattern string from which distances are measured
   * @return an instance for computing distances from the pattern
   */
    public static ModifiedBerghelRoachEditDistance getInstance(CharSequence pattern) {
        return getInstance(pattern.toString());
    }

    /**
   * Creates an instance for computing edit distance from {@code pattern}.
   * @param pattern string from which distances are measured
   * @return an instance for computing distances from the pattern
   */
    public static ModifiedBerghelRoachEditDistance getInstance(String pattern) {
        return new ModifiedBerghelRoachEditDistance(pattern.toCharArray());
    }

    private int[] currentLeft = EMPTY_INT_ARRAY;

    private int[] currentRight = EMPTY_INT_ARRAY;

    private int[] lastLeft = EMPTY_INT_ARRAY;

    private int[] lastRight = EMPTY_INT_ARRAY;

    /**
   * The "pattern" string against which others are compared.
   */
    private final char[] pattern;

    private int[] priorLeft = EMPTY_INT_ARRAY;

    private int[] priorRight = EMPTY_INT_ARRAY;

    private ModifiedBerghelRoachEditDistance(char[] pattern) {
        this.pattern = pattern;
    }

    public ModifiedBerghelRoachEditDistance duplicate() {
        return new ModifiedBerghelRoachEditDistance(pattern);
    }

    public int getDistance(CharSequence targetSequence, int limit) {
        final int targetLength = targetSequence.length();
        final int main = pattern.length - targetLength;
        int distance = Math.abs(main);
        if (distance > limit) {
            return Integer.MAX_VALUE;
        }
        final char[] target = new char[targetLength];
        for (int i = 0; i < targetLength; i++) {
            target[i] = targetSequence.charAt(i);
        }
        if (main <= 0) {
            ensureCapacityRight(distance, false);
            for (int j = 0; j <= distance; j++) {
                lastRight[j] = distance - j - 1;
                priorRight[j] = -1;
            }
        } else {
            ensureCapacityLeft(distance, false);
            for (int j = 0; j <= distance; j++) {
                lastLeft[j] = -1;
                priorLeft[j] = -1;
            }
        }
        boolean even = true;
        while (true) {
            int offDiagonal = (distance - main) / 2;
            ensureCapacityRight(offDiagonal, true);
            if (even) {
                lastRight[offDiagonal] = -1;
            }
            int immediateRight = -1;
            for (; offDiagonal > 0; offDiagonal--) {
                currentRight[offDiagonal] = immediateRight = computeRow((main + offDiagonal), (distance - offDiagonal), pattern, target, priorRight[offDiagonal - 1], lastRight[offDiagonal], immediateRight);
            }
            offDiagonal = (distance + main) / 2;
            ensureCapacityLeft(offDiagonal, true);
            if (even) {
                lastLeft[offDiagonal] = (distance - main) / 2 - 1;
            }
            int immediateLeft = even ? -1 : (distance - main) / 2;
            for (; offDiagonal > 0; offDiagonal--) {
                currentLeft[offDiagonal] = immediateLeft = computeRow((main - offDiagonal), (distance - offDiagonal), pattern, target, immediateLeft, lastLeft[offDiagonal], priorLeft[offDiagonal - 1]);
            }
            int mainRow = computeRow(main, distance, pattern, target, immediateLeft, lastLeft[0], immediateRight);
            if ((mainRow == targetLength) || (++distance > limit) || (distance < 0)) {
                break;
            }
            currentLeft[0] = currentRight[0] = mainRow;
            int[] tmp = priorLeft;
            priorLeft = lastLeft;
            lastLeft = currentLeft;
            currentLeft = priorLeft;
            tmp = priorRight;
            priorRight = lastRight;
            lastRight = currentRight;
            currentRight = tmp;
            even = !even;
        }
        return distance;
    }

    /**
   * Computes the highest row in which the distance {@code p} appears
   * in diagonal {@code k} of the edit distance computation for
   * strings {@code a} and {@code b}.  The diagonal number is
   * represented by the difference in the indices for the two strings;
   * it can range from {@code -b.length()} through {@code a.length()}.
   *
   * More precisely, this computes the highest value x such that
   * <pre>
   *     p = edit-distance(a[0:(x+k)), b[0:x)).
   * </pre>
   *
   * This is the "f" function described by Ukkonen.
   *
   * The caller must assure that abs(k) &le; p, the only values for
   * which this is well-defined.
   *
   * The implementation depends on the cached results of prior
   * computeRow calls for diagonals k-1, k, and k+1 for distance p-1.
   * These must be supplied in {@code knownLeft}, {@code knownAbove},
   * and {@code knownRight}, respectively.
   * @param k diagonal number
   * @param p edit distance
   * @param a one string to be compared
   * @param b other string to be compared
   * @param knownLeft value of {@code computeRow(k-1, p-1, ...)}
   * @param knownAbove value of {@code computeRow(k, p-1, ...)}
   * @param knownRight value of {@code computeRow(k+1, p-1, ...)}
   */
    private int computeRow(int k, int p, char[] a, char[] b, int knownLeft, int knownAbove, int knownRight) {
        assert (Math.abs(k) <= p);
        assert (p >= 0);
        int t;
        if (p == 0) {
            t = 0;
        } else {
            t = Math.max(Math.max(knownAbove, knownRight) + 1, knownLeft);
        }
        int tmax = Math.min(b.length, (a.length - k));
        while ((t < tmax) && b[t] == a[t + k]) {
            t++;
        }
        return t;
    }

    private void ensureCapacityLeft(int index, boolean copy) {
        if (currentLeft.length <= index) {
            index++;
            priorLeft = resize(priorLeft, index, copy);
            lastLeft = resize(lastLeft, index, copy);
            currentLeft = resize(currentLeft, index, false);
        }
    }

    private void ensureCapacityRight(int index, boolean copy) {
        if (currentRight.length <= index) {
            index++;
            priorRight = resize(priorRight, index, copy);
            lastRight = resize(lastRight, index, copy);
            currentRight = resize(currentRight, index, false);
        }
    }

    private int[] resize(int[] array, int size, boolean copy) {
        int[] result = new int[size];
        if (copy) {
            System.arraycopy(array, 0, result, 0, array.length);
        }
        return result;
    }
}
