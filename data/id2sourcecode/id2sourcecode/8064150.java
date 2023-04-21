    @Override
    public final void preRender() {
        frame++;
        timestampPreRender = System.nanoTime();
        if (frame < 2) {
            return;
        }
        lastDeltaSwap = deltaBufferSwap;
        deltaBufferSwap = timestampPreRender - timestampPostRender;
        if (Math.abs(deltaBufferSwap - lastDeltaSwap) < BUFFER_FLUX_TOLERANCE) {
            bufferSwapAvg = (lastDeltaSwap + deltaBufferSwap) / 2;
        }
    }
