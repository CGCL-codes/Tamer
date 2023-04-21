    public int getS(int x, int y) {
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
        if (maxcolor == mincolor) {
            S = 0;
            H = 0;
            return S;
        }
        if (fL < 0.5) fS = (maxcolor - mincolor) / (maxcolor + mincolor); else if (fL >= 0.5) fS = (maxcolor - mincolor) / (2.0f - maxcolor - mincolor);
        if (r == maxcolor) fH = (g - b) / (maxcolor - mincolor);
        if (g == maxcolor) fH = 2.0f + (b - r) / (maxcolor - mincolor);
        if (b == maxcolor) fH = 4.0f + (r - g) / (maxcolor - mincolor);
        S = (int) (fS * 100);
        H = (int) (fH * 60);
        if (H < 0) {
            H += 360;
        }
        return S;
    }
