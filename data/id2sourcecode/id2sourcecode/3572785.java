    private int binarySearchStart(final int time, final int startidx, final int endidx) {
        int high = endidx;
        int low = startidx;
        int probe;
        while (high - low > 1) {
            probe = (high + low) / 2;
            final I2CDataItem di = getItem(probe);
            if (di.getTime() < time) low = probe; else high = probe;
        }
        return low;
    }
