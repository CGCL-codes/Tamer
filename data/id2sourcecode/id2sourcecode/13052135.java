    public int processAudio(AudioBuffer buffer) {
        byte bytes[] = dev.getBuffer();
        if (bytes == null) {
            int n = buffer.getSampleCount();
            for (int ch = 0; ch < chan.length; ch++) {
                float out[] = buffer.getChannel(ch);
                for (int i = 0; i < n; i++) out[i] = 0.0f;
            }
            return AUDIO_OK;
        }
        int n = buffer.getSampleCount();
        int nchan = dev.getChannels();
        if (isBigendian) {
            for (int ch = 0; ch < chan.length; ch++) {
                int chanO = chan[ch];
                float out[] = buffer.getChannel(ch);
                for (int i = 0; i < n; i++) {
                    int ib = i * 2 * nchan + chanO * 2;
                    short sample = (short) ((0xff & bytes[ib + 1]) + ((0xff & bytes[ib]) * 256));
                    float val = sample / 32768f;
                    out[i] = val;
                }
            }
        } else {
            for (int ch = 0; ch < chan.length; ch++) {
                int chanO = chan[ch];
                float out[] = buffer.getChannel(ch);
                for (int i = 0; i < n; i++) {
                    int ib = i * 2 * nchan + chanO * 2;
                    short sample = (short) ((0xff & bytes[ib]) + ((0xff & bytes[ib + 1]) * 256));
                    float val = sample / 32768f;
                    out[i] = val;
                }
            }
        }
        return AUDIO_OK;
    }
