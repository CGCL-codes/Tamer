    static double[] getxy(int r, int g, int b) {
        int[] rdist = new int[] { 4, 0 };
        int[] gdist = new int[] { 0, 6 };
        int[] bdist = new int[] { -4, -8 };
        double x = 0, y = 0;
        if (r == 1 || r == 3 || r == 5) {
            int rr = (r + 1) / 2;
            x -= (rdist[0] / 4.) * rr;
        } else if (r == 2 || r == 4 || r == 6) {
            int rr = 4 - r / 2;
            x += (rdist[0] / 4.) * rr;
        }
        if (g == 1 || g == 3 || g == 5) {
            int rr = (g + 1) / 2;
            y -= (gdist[1] / 4.) * rr;
        } else if (g == 2 || g == 4 || g == 6) {
            int rr = 4 - g / 2;
            y += (gdist[1] / 4.) * rr;
        }
        if (b == 1 || b == 3 || b == 5) {
            int rr = (b + 1) / 2;
            x -= (bdist[0] / 4.) * rr;
            y -= (bdist[1] / 4.) * rr;
        } else if (b == 2 || b == 4 || b == 6) {
            int rr = 4 - b / 2;
            x += (bdist[0] / 4.) * rr;
            y += (bdist[1] / 4.) * rr;
        }
        return new double[] { x, y };
    }
