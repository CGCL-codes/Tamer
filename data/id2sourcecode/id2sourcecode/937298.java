    public int findpd(double Y, int N, double[] X) {
        int JLX = -1;
        int JU = N + 1;
        while (JU - JLX > 1) {
            int JM = (JU + JLX) / 2;
            if (Y > X[JM]) JLX = JM; else JU = JM;
        }
        if (JLX < 0) JLX = 0;
        return JLX;
    }
