    public int processAudio(AudioBuffer buffer) {
        byte bytes[] = dev.getBuffer();
        assert (bytes != null);
        int n = buffer.getSampleCount();
        int nchan = dev.getChannels();
        for (int chPtr = 0; chPtr < chan.length; chPtr++) {
            int ch = chan[chPtr];
            float out[] = buffer.getChannel(chPtr);
            if (isBigEndian) {
                for (int i = 0; i < n; i++) {
                    int ib = i * 2 * nchan + ch * 2;
                    short sample = (short) (out[i] * 32768f);
                    bytes[ib + 1] = (byte) (0xff & sample);
                    bytes[ib] = (byte) (0xff & (sample >> 8));
                }
            } else {
                for (int i = 0; i < n; i++) {
                    int ib = i * 2 * nchan + ch * 2;
                    short sample = (short) (out[i] * 32768f);
                    bytes[ib + 1] = (byte) (0xff & sample >> 8);
                    bytes[ib] = (byte) (0xff & (sample));
                }
            }
        }
        return AUDIO_OK;
    }
