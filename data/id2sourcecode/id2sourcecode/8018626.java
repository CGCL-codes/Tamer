    @Override
    protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf) {
        writeC(buf, getOpCode());
        writeC(buf, 0x40);
        writeH(buf, chatClient.nextIndex());
        writeH(buf, 0x00);
        writeD(buf, channel.getChannelId());
    }
