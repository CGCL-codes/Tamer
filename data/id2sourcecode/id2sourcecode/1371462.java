    private long nd3(long inBottom, long inTop, long inTime) {
        long mid = inBottom + (inTop - inBottom) / 2;
        return (inTime <= mid ? inBottom : inTop);
    }
