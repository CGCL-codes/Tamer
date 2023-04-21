    float trueEdmHeight(int x, int y, ImageProcessor ip) {
        int xmax = width - 1;
        int ymax = ip.getHeight() - 1;
        float[] pixels = (float[]) ip.getPixels();
        int offset = x + y * width;
        float v = pixels[offset];
        if (x == 0 || y == 0 || x == xmax || y == ymax || v == 0) {
            return v;
        } else {
            float trueH = v + 0.5f * SQRT2;
            boolean ridgeOrMax = false;
            for (int d = 0; d < 4; d++) {
                int d2 = (d + 4) % 8;
                float v1 = pixels[offset + dirOffset[d]];
                float v2 = pixels[offset + dirOffset[d2]];
                float h;
                if (v >= v1 && v >= v2) {
                    ridgeOrMax = true;
                    h = (v1 + v2) / 2;
                } else {
                    h = Math.min(v1, v2);
                }
                h += (d % 2 == 0) ? 1 : SQRT2;
                if (trueH > h) trueH = h;
            }
            if (!ridgeOrMax) trueH = v;
            return trueH;
        }
    }
