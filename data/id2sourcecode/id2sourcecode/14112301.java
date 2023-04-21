    public int processAudio(AudioBuffer buffer) {
        if (((AudioControls) vars).isBypassed()) return AUDIO_OK;
        int n = buffer.getSampleCount();
        if (freeverb != null && (reverbBufferIn == null || reverbBufferIn.length != n)) {
            reverbBufferIn = new double[2 * n];
            reverbBufferOut = new double[2 * n];
        }
        int nCh = buffer.getChannelCount();
        float inL[] = buffer.getChannel(0);
        if (nCh == 1) {
            for (int i = 0; i < n; i++) {
                reverbBufferIn[2 * n] = inL[n];
                reverbBufferIn[2 * n + 1] = inL[n];
            }
        } else if (nCh == 2) {
            float inR[] = buffer.getChannel(1);
            for (int i = 0; i < n; i++) {
                reverbBufferIn[2 * i] = inL[i];
                reverbBufferIn[2 * i + 1] = inR[i];
            }
        }
        freeverb.processReplace(reverbBufferIn, reverbBufferOut, 0, 2 * n, 2);
        if (buffer.getChannelCount() == 1) buffer.addChannel(false);
        inL = buffer.getChannel(0);
        float inR[] = buffer.getChannel(1);
        for (int i = 0; i < n; i++) {
            inL[i] = (float) reverbBufferOut[2 * i];
            inR[i] = (float) reverbBufferOut[2 * i + 1];
        }
        return AUDIO_OK;
    }
