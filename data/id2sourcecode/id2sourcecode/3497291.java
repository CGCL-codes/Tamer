    public long getScanOffset(int scanNumber) {
        int high = scanOffsetIndex.length, low = -1, test;
        while (high - low > 1) {
            test = (high + low) / 2;
            if (scanOffsetIndex[test][0] > scanNumber) high = test; else low = test;
        }
        if (low == -1 || scanOffsetIndex[low][0] != scanNumber) return -1; else return scanOffsetIndex[low][1];
    }
