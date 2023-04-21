    public double getChannelLevel(int ch) {
        if (ch >= 0 && ch < MixerComponent.MIXER_CHANNELS) {
            return mixLevels[ch];
        }
        return 0.0;
    }
