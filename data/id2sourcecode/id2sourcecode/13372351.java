    public float getChannelWeight(int chan) {
        if (chan >= 0 && chan < NUM_CHANS) return channelWeights[chan]; else return 0;
    }
