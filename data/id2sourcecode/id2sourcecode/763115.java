    public int getChannel(int channelNum) throws IOException {
        if (channelNum >= numChannels || channelNum < 0) {
            throw new IOException();
        }
        return channel[channelNum];
    }
