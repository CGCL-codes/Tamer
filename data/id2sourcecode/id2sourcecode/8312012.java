    public static int[][] conv9to4(int[][] ft) {
        int[][] r = new int[2][2];
        r[0][0] = ft[0][0];
        r[0][1] = ft[0][2];
        r[1][1] = ft[2][2];
        r[1][0] = ft[2][0];
        return r;
    }
