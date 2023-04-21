    public int getChannelNumber() {
        switch(channelMode) {
            case CHANNEL_MODE_DUAL_CHANNEL:
                return 2;
            case CHANNEL_MODE_JOINT_STEREO:
                return 2;
            case CHANNEL_MODE_MONO:
                return 1;
            case CHANNEL_MODE_STEREO:
                return 2;
        }
        return 0;
    }
