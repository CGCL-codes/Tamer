    private static int getRightTangentIndex(Coordinate c, Coordinate[] ccwRing) {
        int n = ccwRing.length - 1;
        if (isBelow(ccwRing[1], ccwRing[0], c) && !isAbove(ccwRing[n - 1], ccwRing[0], c)) {
            boolean tt = !isAbove(ccwRing[n - 1], ccwRing[0], c);
            tt = !tt;
            return 0;
        }
        int a = 0;
        int b = n;
        for (int i = 0; i <= n; i++) {
            int m = (a + b) / 2;
            boolean downM = isBelow(ccwRing[m + 1], ccwRing[m], c);
            if (downM && !isAbove(ccwRing[m - 1], ccwRing[m], c)) {
                return m;
            }
            boolean upA = isAbove(ccwRing[a + 1], ccwRing[a], c);
            if (upA) {
                if (downM) {
                    b = m;
                } else {
                    if (isAbove(ccwRing[a], ccwRing[m], c)) {
                        b = m;
                    } else {
                        a = m;
                    }
                }
            } else {
                if (!downM) {
                    a = m;
                } else {
                    if (isBelow(ccwRing[a], ccwRing[m], c)) {
                        b = m;
                    } else {
                        a = m;
                    }
                }
            }
        }
        return (a + b) / 2;
    }
