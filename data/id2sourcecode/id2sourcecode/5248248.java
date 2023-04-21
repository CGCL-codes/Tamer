    @Override
    public DoublePlane xGradient() {
        DoublePlane plane = new DoublePlane(getSize());
        double[] src = doubleArray();
        double[] dst = plane.doubleArray();
        int w = getWidth();
        int h = getHeight();
        for (int y = 0; y < h; y++) {
            int row = y * w;
            int end = row + w - 1;
            for (int x = row; x < end; x++) {
                dst[x] = src[x + 1] - src[x];
            }
        }
        return plane;
    }
