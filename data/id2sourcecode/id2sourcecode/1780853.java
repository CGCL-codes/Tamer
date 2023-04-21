    protected ChannelFormat guessFormat() {
        switch(getChannelCount()) {
            case 1:
                return ChannelFormat.MONO;
            case 2:
                return ChannelFormat.STEREO;
            case 4:
                return ChannelFormat.QUAD;
            case 6:
                return ChannelFormat.FIVE_1;
        }
        return ChannelFormat.STEREO;
    }
