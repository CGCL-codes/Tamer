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
