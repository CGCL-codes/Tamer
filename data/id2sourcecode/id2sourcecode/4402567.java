    private static float[] RGBtoHSL(int r, int g, int b) {
        float R = (float) r / 255;
        float G = (float) g / 255;
        float B = (float) b / 255;
        float min = Math.min(Math.min(R, G), B);
        float max = Math.max(Math.max(R, G), B);
        float delta = max - min;
        float L = (max + min) / 2;
        float H, S;
        if (delta == 0) {
            H = 0;
            S = 0;
        } else {
            if (L < 0.5f) {
                S = delta / (max + min);
            } else {
                S = delta / (2 - max - min);
            }
            float deltaR = (((max - R) / 6) + (delta / 2)) / delta;
            float deltaG = (((max - G) / 6) + (delta / 2)) / delta;
            float deltaB = (((max - B) / 6) + (delta / 2)) / delta;
            if (R == max) {
                H = deltaB - deltaG;
            } else if (G == max) {
                H = (1f / 3) + deltaR - deltaB;
            } else {
                H = (2f / 3) + deltaG - deltaR;
            }
            if (H < 0) {
                H++;
            }
            if (H > 1) {
                H--;
            }
        }
        return new float[] { H, S, L };
    }
