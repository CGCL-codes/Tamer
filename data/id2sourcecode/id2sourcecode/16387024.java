    protected final int findMinorIndex(double x) {
        int maxTrials = 2, n = data.length, i = n - 1, i1 = 0, i2 = i;
        while (data[i] == null) {
            i = i1 + (i2 - i1) / 2;
            if (data[i] == null) i2 = i; else {
                i1 = i;
                do {
                    i = i2 - (i2 - i) / 2;
                } while (data[i] != null && i < i2);
                if (i == i2) {
                    i = i2 - 1;
                } else i2 = i;
            }
        }
        n = i + 1;
        i = 0;
        if (x > x1) {
            i = lastN + 2;
            int iMax = i + maxTrials;
            if (iMax > n) iMax = n;
            while (i < iMax && x > data[i][0]) i++;
            if (x <= data[i][0]) return i - 1;
            i--;
        } else if (x < x0) {
            i = lastN - 1;
            int iMin = i - maxTrials;
            if (iMin < -1) iMin = -1;
            while (i > iMin && x < data[i][0]) i--;
            if (x >= data[i][0]) return i;
        }
        i1 = x > x1 ? i : 0;
        i2 = x > x1 ? n - 1 : i;
        double d = i2 - i1;
        while (d > 1.) {
            i = i1 + (int) Math.round(d * 0.5);
            if (x > data[i][0]) i1 = i; else i2 = i;
            d = i2 - i1;
        }
        return i1;
    }
