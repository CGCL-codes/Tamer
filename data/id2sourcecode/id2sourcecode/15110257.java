    public float getPeakLevel(int channelIndex) {
        float peak = 0;
        float actual = 0;
        switch(state) {
            case PLAY:
                {
                    int max = clip.getMaxSampleLength();
                    for (int i = playPointer; i < playPointer + playPacketSize; i++) {
                        if (player != null) {
                            actual = Math.abs(player.getSample(channelIndex, i % max));
                        }
                        if (actual > peak) {
                            peak = actual;
                        }
                    }
                    return peak;
                }
            case REC:
                {
                    AChannel s = clip.getSelectedLayer().getChannel(channelIndex);
                    int max = clip.getSelectedLayer().getMaxSampleLength();
                    for (int i = playPointer; i < playPointer + capturePacketSize; i++) {
                        actual = Math.abs(s.getSample(i % max));
                        if (actual > peak) {
                            peak = actual;
                        }
                    }
                    return peak;
                }
        }
        return 0;
    }
