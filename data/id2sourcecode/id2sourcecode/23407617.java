    public int processAudio(AudioBuffer buffer) {
        float left[] = buffer.getChannel(0);
        float right[] = buffer.getChannel(1);
        for (int n = 0; n < buffer.getSampleCount() && metSamplePos < sampleData.length; n++) {
            left[n] += sampleData[metSamplePos] * level;
            right[n] += sampleData[metSamplePos++] * level;
        }
        return AUDIO_OK;
    }
