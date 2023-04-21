    public static void RGBtoHSL(float r, float g, float b, float[] ret) {
        r /= 255;
        g /= 255;
        b /= 255;
        if (r > 1) r = 1;
        if (g > 1) g = 1;
        if (b > 1) b = 1;
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));
        float h = 0;
        if (max == min) h = 0; else if (max == r) h = ((60 * (g - b) / (max - min)) + 360) % 360; else if (max == g) h = (60 * (b - r) / (max - min)) + 120; else if (max == b) h = (60 * (r - g) / (max - min)) + 240;
        float l = (max + min) / 2;
        float s = 0;
        if (max == min) s = 0; else if (l <= .5f) s = (max - min) / (max + min); else s = (max - min) / (2 - max - min);
        ret[0] = h / 360;
        ret[1] = s;
        ret[2] = l;
    }
