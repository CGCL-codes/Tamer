    public int processAudio(AudioBuffer buffer) {
        if (!active) {
            return AUDIO_OK;
        }
        if (!(doClick > 0 || project.getSequencer().isRunning())) return AUDIO_OK;
        int size = buffer.getSampleCount();
        int start = 0;
        long nextClick = nextClick();
        if (metSamplePos >= sampleData.length) {
            doClick--;
            if (framePtr + size < nextClick) {
                framePtr += size;
                return AUDIO_OK;
            }
            start = (int) (nextClick - framePtr);
            metSamplePos = 0;
        }
        float left[] = buffer.getChannel(0);
        float right[] = buffer.getChannel(1);
        framePtr += start;
        for (int n = start; (n < size) && (metSamplePos < sampleData.length); n++, framePtr++) {
            if (framePtr == nextClick) {
                metSamplePos = 0;
            }
            left[n] += sampleData[metSamplePos] * level;
            right[n] += sampleData[metSamplePos++] * level;
        }
        return AUDIO_OK;
    }
