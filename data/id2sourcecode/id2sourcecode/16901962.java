    private int indexOfSegment(double x) {
        if (x < minX || x >= maxX) throw new IndexOutOfBoundsException("X(" + x + ") is not valid for function");
        int lowIdx = 0;
        int highIdx = segmentCount - 1;
        while (lowIdx <= highIdx) {
            int idx = (lowIdx + highIdx) / 2;
            if (x < pointX[idx]) highIdx = idx - 1; else if (x < pointX[idx + 1]) return idx; else lowIdx = idx + 1;
        }
        throw new IllegalStateException("Point " + x + " was not located for function");
    }
