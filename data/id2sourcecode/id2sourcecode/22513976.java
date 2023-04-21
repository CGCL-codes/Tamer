    protected void fill(AudioBuffer buffer, int startChunk, int endChunk) {
        if (nChannels == 2) {
            float[] left = buffer.getChannel(0);
            float[] right = buffer.getChannel(1);
            for (int n = startChunk / 2; n < endChunk / 2; n++) {
                float sample = ((short) ((0xff & byteBuff[(n * 2) + 0]) + ((0xff & byteBuff[(n * 2) + 1]) * 256)) / 32768f);
                if (n % 2 == 0) left[n / 2] += sample; else right[n / 2] += sample;
            }
        } else {
            float[] left = buffer.getChannel(0);
            for (int n = startChunk; n < endChunk; n += 2) {
                float val = ((short) ((0xff & byteBuff[n]) + ((0xff & byteBuff[n + 1]) * 256)) / 32768f);
                left[n / 2] += val;
            }
        }
    }
