    void squareStep(int x0, int y0, int x1, int y1) {
        int x = (x0 + x1) / 2;
        int y = (y0 + y1) / 2;
        float cx0 = getPixel(x0, y);
        float cx1 = getPixel(x1, y);
        float cy0 = getPixel(x, y0);
        float cy1 = getPixel(x, y1);
        float c = (cx0 + cx1 + cy0 + cy1) / 4;
        c += noise * (rnd.nextFloat() - 0.5);
        setPixel(x, y, c);
    }
