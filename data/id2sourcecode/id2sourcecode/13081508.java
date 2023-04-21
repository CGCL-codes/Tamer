    public int getL(int x, int y) {
        int H = 0;
        int S = 0;
        int L = 0;
        float fH = 0;
        float fS = 0;
        float fL = 0;
        float r = (float) getRed(x, y) / 255;
        float g = (float) getGreen(x, y) / 255;
        float b = (float) getBlue(x, y) / 255;
        float maxcolor = r > g ? r : g;
        maxcolor = b > maxcolor ? b : maxcolor;
        float mincolor = r < g ? r : g;
        mincolor = b < mincolor ? b : mincolor;
        fL = (maxcolor + mincolor) / 2;
        L = (int) (fL * 100);
        return L;
    }
