    public int processAudio(AudioBuffer buffer) {
        if (vars.isBypassed()) return AUDIO_OK;
        int nsamples = buffer.getSampleCount();
        buffer.monoToStereo();
        float otherFactor = vars.getWidthFactor();
        boolean swap = vars.isLRSwapped();
        ChannelFormat format = buffer.getChannelFormat();
        int[] leftChans = format.getLeft();
        int[] rightChans = format.getRight();
        float tmp;
        for (int pair = 0; pair < leftChans.length; pair++) {
            float[] left = buffer.getChannel(leftChans[pair]);
            float[] right = buffer.getChannel(rightChans[pair]);
            for (int i = 0; i < nsamples; i++) {
                tmp = left[i];
                left[i] += otherFactor * right[i];
                right[i] += otherFactor * tmp;
            }
            if (swap) buffer.swap(leftChans[pair], rightChans[pair]);
        }
        return AUDIO_OK;
    }
