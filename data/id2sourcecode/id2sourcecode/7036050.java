    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Header)) {
            return false;
        }
        final Header header = (Header) other;
        return (header.getChannelId() == channelId && header.getDataType() == dataType && header.getSize() == size && header.getTimer() == timer && header.getStreamId() == streamId);
    }
