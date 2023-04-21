    public float square() {
        int ns = getSampleCount();
        int nc = getChannelCount();
        float sumOfSquares = 0f;
        float[] samples;
        for (int c = 0; c < nc; c++) {
            samples = getChannel(c);
            for (int s = 0; s < ns; s++) {
                float sample = samples[s];
                sumOfSquares += sample * sample;
            }
        }
        return sumOfSquares / (nc * ns);
    }
