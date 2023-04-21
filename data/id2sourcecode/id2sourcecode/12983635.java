    public Channel getChannel(boolean isImport, boolean isKwh) {
        Channel channel = null;
        for (Channel candidateChannel : channels) {
            if (candidateChannel.getIsImport() == isImport && candidateChannel.getIsKwh() == isKwh) {
                channel = candidateChannel;
                break;
            }
        }
        return channel;
    }
