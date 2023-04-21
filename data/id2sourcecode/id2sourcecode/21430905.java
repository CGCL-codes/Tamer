    private byte[] getChannelData(int channelId) {
        if (uncompressedChannels == null) {
            for (Channel c : channels) {
                if (channelId == c.getId() && c.getCompressedData() != null) {
                    ChannelUncompressor uncompressor = new ChannelUncompressor();
                    byte[] uncompressedChannel = uncompressor.uncompress(c.getCompressedData(), width, height);
                    if (uncompressedChannel != null) {
                        return uncompressedChannel;
                    }
                }
            }
        } else {
            if (channelId >= 0 && uncompressedChannels[channelId] != null) {
                return uncompressedChannels[channelId];
            }
        }
        return fillBytes(width * height, (byte) (channelId == Channel.ALPHA ? 255 : 0));
    }
