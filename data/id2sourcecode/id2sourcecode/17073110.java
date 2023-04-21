    @Override
    protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf) {
        writeC(buf, getOpCode());
        writeC(buf, 0x00);
        writeD(buf, message.getChannel().getChannelId());
        writeD(buf, message.getSender().getClientId());
        writeD(buf, 0x00);
        writeH(buf, message.getSender().getIdentifier().length / 2);
        writeB(buf, message.getSender().getIdentifier());
        writeH(buf, message.size() / 2);
        writeB(buf, message.getText());
    }
