    public void drawFPS(Graphics2D g2) {
        for (int i = 0; i < fpsAverageCount - 1; ++i) {
            nanos[i] = nanos[i + 1];
        }
        nanos[fpsAverageCount - 1] = System.nanoTime();
        float averagedFPS = (float) ((fpsAverageCount - 1) * 1000000000.0 / (nanos[fpsAverageCount - 1] - nanos[0]));
        ++frameCount;
        float totalFPS = (float) (frameCount * 1000000000 / (1.0 * (System.nanoTime() - nanoStart)));
        g2.drawString("100 Average FPS is " + averagedFPS, 15, 15);
        g2.drawString("Entire FPS is " + totalFPS, 15, 25);
    }
