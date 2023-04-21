    @Override
    public Bitmap apply(Bitmap source) {
        Bitmap dest = source.clone();
        int w = dest.getWidth();
        int h = dest.getHeight();
        for (int x = 0; x < w; x++) for (int y = 0; y < h; y++) {
            int px = dest.getPixel(x, y);
            int l = mRand.nextInt(mLevel + 1);
            int nx = x + l;
            if (nx >= w) nx = w - 1;
            int ny = y + l;
            if (ny >= h) ny = h - 1;
            dest.setPixel(nx, ny, px);
        }
        return dest;
    }
