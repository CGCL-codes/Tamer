    void diamondStep(int x0, int y0, int x1, int y1) {
        int w = x1 - x0;
        int h = y1 - y0;
        if (w < 2 || h < 2) return;
        int x = (x0 + x1) / 2;
        int y = (y0 + y1) / 2;
        float c00 = getPixel(x0, y0);
        float c01 = getPixel(x1, y0);
        float c10 = getPixel(x0, y1);
        float c11 = getPixel(x1, y1);
        float c = (c00 + c01 + c10 + c11) / 4;
        c += noise * (rnd.nextFloat() - 0.5);
        setPixel(x, y, c);
    }
