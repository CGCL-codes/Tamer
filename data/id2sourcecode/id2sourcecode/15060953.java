    private static int getLeftTangentIndex(Coordinate c, Coordinate[] ccwRing) {
        int n = ccwRing.length - 1;
        if (isAbove(ccwRing[n - 1], ccwRing[0], c) && !isBelow(ccwRing[1], ccwRing[0], c)) {
            return 0;
        }
        int a = 0;
        int b = n;
        for (int i = 0; i <= n; i++) {
            int m = (a + b) / 2;
            boolean downM = isBelow(ccwRing[m + 1], ccwRing[m], c);
            if (isAbove(ccwRing[m - 1], ccwRing[m], c) && !downM) {
                return m;
            }
            boolean downA = isBelow(ccwRing[a + 1], ccwRing[a], c);
            if (downA) {
                if (!downM) {
                    b = m;
                } else {
                    if (isBelow(ccwRing[a], ccwRing[m], c)) {
                        b = m;
                    } else {
                        a = m;
                    }
                }
            } else {
                if (downM) {
                    a = m;
                } else {
                    if (isAbove(ccwRing[a], ccwRing[m], c)) {
                        b = m;
                    } else {
                        a = m;
                    }
                }
            }
        }
        return (a + b) / 2 - 1;
    }
