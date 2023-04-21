    public static double exactPValue(int nX, int nY, double u) {
        int countw = 0;
        int n = nX + nY;
        int firstW = (int) (.5 * nX * (nX + 1));
        int lastW = 0;
        for (int i = n; i > n - nX; i--) {
            lastW = i + lastW;
        }
        double middle = (firstW + lastW) / 2;
        if (u <= middle) {
            return (cummulativeLE(nX, nY, u));
        } else {
            return (cummulativeGE(nX, nY, (firstW + lastW) - u));
        }
    }
