    protected final float getSample(int channelIndex, int sampleIndex) {
        float s = 0;
        for (int i = 0; i < clip.getNumberOfLayers(); i++) {
            ALayer l = clip.getLayer(i);
            if (l.getType() == ALayer.AUDIO_LAYER) {
                AChannel ch = l.getChannel(channelIndex);
                s += ch.getMaskedSample(sampleIndex);
            }
        }
        return s;
    }
