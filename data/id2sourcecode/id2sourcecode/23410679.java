    private int findGridIndex(Number x, int startPos, int lastPos) {
        int diff = lastPos - startPos;
        if (diff <= 0) return -1;
        int compareStartPos = x.compareTo(dots[startPos][0]);
        int compareLastPos = x.compareTo(dots[lastPos][0]);
        if (compareStartPos == -1 || compareLastPos == 1) return -1;
        if (compareStartPos == 0 || diff == 1) return startPos;
        if (compareLastPos == 0) return lastPos - 1;
        int middlePos = (startPos + lastPos) / 2;
        int compareMiddlePos = x.compareTo(dots[middlePos][0]);
        if (compareMiddlePos == 0) return middlePos;
        if (compareMiddlePos == -1) return findGridIndex(x, startPos, middlePos);
        return findGridIndex(x, middlePos, lastPos);
    }
