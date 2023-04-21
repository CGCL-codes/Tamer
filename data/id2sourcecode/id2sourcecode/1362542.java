    public final float getSample(int channelIndex, int sampleIndex) {
        float s = 0;
        for (int i = 0; i < clip.getNumberOfLayers(); i++) {
            ALayer l = clip.getLayer(i);
            switch(l.getType()) {
                case ALayer.AUDIO_LAYER:
                    s += l.getChannel(channelIndex).getMaskedSample(sampleIndex);
                    break;
                case ALayer.SOLO_AUDIO_LAYER:
                    s = l.getChannel(channelIndex).getMaskedSample(sampleIndex);
                    return s;
            }
        }
        return s;
    }
