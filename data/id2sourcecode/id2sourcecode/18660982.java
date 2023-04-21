    private final void initDensityStencil() {
        float center = (image_dim + 1) / 2;
        float center2 = center * center;
        float center4 = center2 * center2;
        float center6 = center4 * center2;
        float columnDistance = 0;
        float rowDistance = 0;
        float distance2 = 0;
        int y = 0;
        int opacity = 0;
        for (int x = 0; x < stencilNoise.length; x++) {
            for (y = 0; y < stencilNoise[0].length; y++) {
                columnDistance = (x - center) * (x - center);
                rowDistance = (y - center) * (y - center);
                distance2 = rowDistance + columnDistance;
                if (distance2 > center2) {
                    opacity = 0;
                } else {
                    float distance4 = distance2 * distance2;
                    float distance6 = distance4 * distance2;
                    opacity = (int) (255 * (1 - (4 / 9) * (distance6 / center6) + (17 / 9) * (distance4 / center4) - (22 / 9) * (distance2 / center2)));
                }
                stencilNoise[x][y] = (byte) Math.min(255, opacity);
            }
        }
    }
