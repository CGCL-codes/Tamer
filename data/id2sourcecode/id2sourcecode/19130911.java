    private double getMidSummarys(Vector<Double> data) {
        int size = data.size() - 1;
        double m;
        double next;
        Vector<Double> mids = new Vector<Double>();
        double temp;
        int i;
        m = (size + 1) / 2;
        if (m % 2 > 0) {
            mids.add(0.5 * (data.get((int) (m - 0.5)) + data.get((int) (m + 0.5))));
        } else {
            mids.add(data.get((int) m));
        }
        boolean go = false;
        next = m;
        while (!go) {
            next = (Math.floor(next) + 1) / 2;
            if (next % 2 == 0) {
                temp = (data.get((int) next) + data.get((int) (size - next + 1))) / 2;
                mids.add(temp);
                if (next == 1) go = true;
            } else {
                temp = (0.5 * (data.get((int) (next - 0.5)) + data.get((int) (next + 0.5)))) + (0.5 * (data.get((int) (size - next + 0.5)) + data.get((int) (size - next + 1.5)))) / 2;
                mids.add(temp);
                if (next == 1.5) go = true;
            }
        }
        double sum1 = 0;
        double xsquaresum = 0;
        double xsum = 0;
        double msize = mids.size();
        for (i = 0; i < msize; i++) {
            sum1 += (i + 1) * mids.get(i);
            xsquaresum += (i + 1) * (i + 1);
            xsum += (i + 1);
        }
        return (sum1 - msize * (xsum / msize) * Univariate.getMean2(mids)) / (xsquaresum - msize * ((xsum / msize) * (xsum / msize)));
    }
