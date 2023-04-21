    public Channel getChannel(byte no) {
        if (no < TRACUtil.ZERO + 1 || no > TRACUtil.ZERO + TRACUtil.MAXCHANNELS) return null;
        if (fileChannels[no - TRACUtil.ZERO] == null) fileChannels[no - TRACUtil.ZERO] = new Channel(no);
        return fileChannels[no - TRACUtil.ZERO];
    }
