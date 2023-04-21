    public void changeSampleCount(int newSampleCount, boolean keepOldSamples) {
        int oldSampleCount = getSampleCount();
        if (oldSampleCount == newSampleCount) {
            return;
        }
        Object[] oldChannels = null;
        if (keepOldSamples) {
            oldChannels = getAllChannels();
        }
        init(getChannelCount(), newSampleCount, getSampleRate());
        if (keepOldSamples) {
            int copyCount = newSampleCount < oldSampleCount ? newSampleCount : oldSampleCount;
            for (int ch = 0; ch < getChannelCount(); ch++) {
                float[] oldSamples = (float[]) oldChannels[ch];
                float[] newSamples = (float[]) getChannel(ch);
                if (oldSamples != newSamples) {
                    System.arraycopy(oldSamples, 0, newSamples, 0, copyCount);
                }
                if (oldSampleCount < newSampleCount) {
                    for (int i = oldSampleCount; i < newSampleCount; i++) {
                        newSamples[i] = 0.0f;
                    }
                }
            }
        }
    }
