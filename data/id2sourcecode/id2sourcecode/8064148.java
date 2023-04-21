    @Override
    public final void postRender() {
        timestampPostRender = System.nanoTime();
        deltaNanosLast = deltaNanos;
        deltaNanos = timestampPostRender - timestampPreRender;
        if (Math.abs(deltaNanos - deltaNanosLast) < FLUX_TOLERANCE) {
            deltaNanosAvg = (deltaNanos + deltaNanosLast) / 2;
        }
        if (fpsCounter) {
            drawFPS();
        }
        postRender2();
    }
