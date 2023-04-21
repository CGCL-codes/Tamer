    CommChannel getChannel(int aChannel) {
        if (aChannel < 0 || aChannel >= fTransportAttribute.fNumOfChannels) return null;
        return fChannelArray.get(aChannel);
    }
