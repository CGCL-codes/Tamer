    private final void initRoundedStencil() {
        int y = 0;
        float noise = 0;
        float dist = 0;
        float value1 = 0;
        int opacity = 0;
        final float image_radius = (image_dim + 1) / 2;
        float weight = 0;
        final float distance2RandomRatio = .75f;
        final float stencilZoom = 50f;
        final Point2f center = new Point2f(image_radius, image_radius);
        final Point2f point = new Point2f();
        float max = (float) Math.sqrt((image_radius * image_radius) * 2);
        for (int x = 0; x < stencilNoise.length; x++) {
            for (y = 0; y < stencilNoise[0].length; y++) {
                noise = (float) ImprovedNoise.fBm(0, x / stencilZoom, y / stencilZoom, 2, 4);
                point.set(x, y);
                dist = center.distance(point);
                if (dist < image_radius - 10) {
                    stencilNoise[x][y] = (byte) 255;
                    continue;
                }
                value1 = (1 - dist / max) * 2f;
                weight = distance2RandomRatio * value1 + (1 - distance2RandomRatio) * noise;
                opacity = (int) (value1 * Math.min(255, (255 * weight)));
                stencilNoise[x][y] = (byte) Math.min(255, opacity);
            }
        }
    }
