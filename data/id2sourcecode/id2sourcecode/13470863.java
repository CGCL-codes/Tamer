    public static float[] RGBtoHLS(float R, float G, float B) {
        float cHi = Math.max(R, Math.max(G, B));
        float cLo = Math.min(R, Math.min(G, B));
        float cRng = cHi - cLo;
        float L = (cHi + cLo) / 2;
        float S = 0;
        if (0 < L && L < 1) {
            float d = (L <= 0.5f) ? L : (1 - L);
            S = 0.5f * cRng / d;
        }
        float H = 0;
        if (cHi > 0 && cRng > 0) {
            float rr = (cHi - R) / cRng;
            float gg = (cHi - G) / cRng;
            float bb = (cHi - B) / cRng;
            float hh;
            if (R == cHi) hh = bb - gg; else if (G == cHi) hh = rr - bb + 2.0f; else hh = gg - rr + 4.0f;
            if (hh < 0) hh = hh + 6;
            H = hh / 6;
        }
        return new float[] { H, L, S };
    }
