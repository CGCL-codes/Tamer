    @Override
    public FloatPlane xGradient() {
        FloatPlane plane = new FloatPlane(getSize());
        float[] src = floatArray();
        float[] dst = plane.floatArray();
        int w = getWidth();
        int h = getHeight();
        for (int y = 0; y < h; y++) {
            int row = y * w;
            int end = row + w - 1;
            for (int x = row; x < end; x++) {
                dst[x] = src[x + 1] - src[x] + 0.5f;
            }
        }
        return plane;
    }
