    private int findPositions(TPoint p) {
        int i = 0;
        int j = this.series.size();
        int k = 0;
        while ((j - i) > 1) {
            k = i + (j - i) / 2;
            if (p.tstamp() == this.series.elementAt(k).tstamp()) {
                return k;
            } else if (p.tstamp() < this.series.elementAt(k).tstamp()) {
                j = k;
            } else {
                i = k;
            }
        }
        if ((j - i) == 1) {
            return j;
        }
        return -1;
    }
