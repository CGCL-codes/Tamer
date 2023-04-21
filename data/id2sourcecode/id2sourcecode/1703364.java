    public int processAudio(AudioBuffer buffer) {
        float out[] = buffer.getChannel(0);
        byte bytes[] = dev.getBuffer();
        if (bytes == null) {
            int n = buffer.getSampleCount();
            for (int i = 0; i < n; i++) out[i] = 0.0f;
            return AUDIO_OK;
        }
        int n = buffer.getSampleCount();
        int nchan = dev.getChannels();
        if (isBigendian) {
            for (int i = 0; i < n; i++) {
                int ib = i * 2 * nchan + chan * 2;
                short sample = (short) ((0xff & bytes[ib + 1]) + ((0xff & bytes[ib]) * 256));
                float val = sample / 32768f;
                out[i] = val;
            }
        } else {
            for (int i = 0; i < n; i++) {
                int ib = i * 2 * nchan + chan * 2;
                short sample = (short) ((0xff & bytes[ib]) + ((0xff & bytes[ib + 1]) * 256));
                float val = sample / 32768f;
                out[i] = val;
            }
        }
        return AUDIO_OK;
    }
