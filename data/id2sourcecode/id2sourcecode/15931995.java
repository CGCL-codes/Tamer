    public int getChannelDepth(int channelIndex) {
        if (channelIndex == 0) {
            return 32;
        } else {
            throw new ArrayIndexOutOfBoundsException(channelIndex);
        }
    }
