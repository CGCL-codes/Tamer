    public static HXPoint to2D(int x, int y, int z) {
        int j = x;
        int i = (y + z) / 2;
        return new HXPoint(i, j);
    }
