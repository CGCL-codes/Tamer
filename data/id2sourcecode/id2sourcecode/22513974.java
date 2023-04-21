    protected void fillLinearInterpolate(AudioBuffer buffer, int startChunk, int endChunk, double gain1, double gain2) {
        double dG = (gain2 - gain1) / (endChunk - startChunk) / nChannels / 2.0;
        if (nChannels == 2) {
            float[] left = buffer.getChannel(0);
            float[] right = buffer.getChannel(1);
            for (int n = startChunk / 2; n < endChunk / 2; n++) {
                float sample = ((short) ((0xff & byteBuff[(n * 2) + 0]) + ((0xff & byteBuff[(n * 2) + 1]) * 256)) / 32768f);
                sample *= gain1;
                if (n % 2 == 0) left[n / 2] += sample; else right[n / 2] += sample;
                gain1 += dG;
            }
        } else {
            float[] left = buffer.getChannel(0);
            for (int n = startChunk; n < endChunk; n += 2) {
                float val = ((short) ((0xff & byteBuff[n]) + ((0xff & byteBuff[n + 1]) * 256)) / 32768f);
                left[n / 2] += val * gain1;
                gain1 += dG;
            }
        }
    }
