    public int processAudio(AudioBuffer buffer) {
        if (!vars.isEnabled() && vars.isMaster()) {
            buffer.makeSilence();
        } else if (vars.isEnabled()) {
            gain = vars.getGain();
            if (gain > 0f || vars.isMaster()) {
                vars.getChannelGains(channelGains);
                for (int c = 0; c < channelGains.length; c++) {
                    smoothedChannelGains[c] += factor * (channelGains[c] - smoothedChannelGains[c]);
                }
                getRoutedStrip().mix(buffer, smoothedChannelGains);
            }
        }
        return AUDIO_OK;
    }
