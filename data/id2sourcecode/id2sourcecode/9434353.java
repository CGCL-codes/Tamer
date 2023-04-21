    public int processAudio(AudioBuffer buffer) {
        int nc = buffer.getChannelCount();
        int ns = buffer.getSampleCount();
        float[] array;
        check(buffer);
        for (int c = 0; c < nc; c++) {
            array = buffer.getChannel(c);
            detectOvers(c, array, ns);
            detectPeak(c, array, ns);
            detectAverage(c, array, ns);
        }
        return AUDIO_OK;
    }
