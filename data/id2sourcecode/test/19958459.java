    @Override
    public double calculateDistance(double[] value1, double[] value2) {
        ArrayList<Double> l1 = new ArrayList<Double>();
        ArrayList<Double> l2 = new ArrayList<Double>();
        int i, j;
        for (i = 0; i < value1.length; i++) {
            double value = value1[i];
            if (!Double.isNaN(value)) {
                l1.add(value);
            }
        }
        for (i = 0; i < value2.length; i++) {
            double value = value2[i];
            if (!Double.isNaN(value)) {
                l2.add(value);
            }
        }
        double[] ts1 = new double[l1.size()];
        double[] ts2 = new double[l2.size()];
        for (i = 0; i < ts1.length; i++) {
            ts1[i] = l1.get(i);
        }
        for (i = 0; i < ts2.length; i++) {
            ts2[i] = l2.get(i);
        }
        double[][] dP2P = new double[ts1.length][ts2.length];
        for (i = 0; i < ts1.length; i++) {
            for (j = 0; j < ts2.length; j++) {
                dP2P[i][j] = pointDistance(i, j, ts1, ts2);
            }
        }
        if (ts1.length == 0 || ts2.length == 0) {
            return Double.NaN;
        }
        if (ts1.length == 1 && ts2.length == 1) {
            return (Math.sqrt(dP2P[0][0]));
        }
        double[][] D = new double[ts1.length][ts2.length];
        D[0][0] = dP2P[0][0];
        for (i = 1; i < ts1.length; i++) {
            D[i][0] = dP2P[i][0] + D[i - 1][0];
        }
        if (ts2.length == 1) {
            double sum = 0;
            for (i = 0; i < ts1.length; i++) {
                sum += D[i][0];
            }
            return (Math.sqrt(sum) / ts1.length);
        }
        for (j = 1; j < ts2.length; j++) {
            D[0][j] = dP2P[0][j] + D[0][j - 1];
        }
        if (ts1.length == 1) {
            double sum = 0;
            for (j = 0; j < ts2.length; j++) {
                sum += D[0][j];
            }
            return (Math.sqrt(sum) / ts2.length);
        }
        for (i = 1; i < ts1.length; i++) {
            for (j = 1; j < ts2.length; j++) {
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] };
                double min = Math.min(steps[0], Math.min(steps[1], steps[2]));
                D[i][j] = dP2P[i][j] + min;
            }
        }
        i = ts1.length - 1;
        j = ts2.length - 1;
        int k = 1;
        double dist = D[i][j];
        while (i + j > 2) {
            if (i == 0) {
                j--;
            } else if (j == 0) {
                i--;
            } else {
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] };
                double min = Math.min(steps[0], Math.min(steps[1], steps[2]));
                if (min == steps[0]) {
                    i--;
                    j--;
                } else if (min == steps[1]) {
                    i--;
                } else if (min == steps[2]) {
                    j--;
                }
            }
            k++;
            dist += D[i][j];
        }
        return (Math.sqrt(dist) / k);
    }
