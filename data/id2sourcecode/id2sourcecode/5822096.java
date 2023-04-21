    public int processAudio(AudioBuffer buffer) {
        boolean bypassed = specification.isBypassed();
        if (bypassed) {
            if (!wasBypassed) {
                clear();
                wasBypassed = true;
            }
            return AUDIO_OK;
        }
        int newRate = (int) buffer.getSampleRate();
        if (sampleRate != newRate) {
            sampleRate = newRate;
            updateDesigns();
        }
        int nc = buffer.getChannelCount();
        int ns = buffer.getSampleCount();
        for (int c = 0; c < nc; c++) {
            filter(buffer.getChannel(c), ns, c);
        }
        wasBypassed = bypassed;
        return AUDIO_OK;
    }
