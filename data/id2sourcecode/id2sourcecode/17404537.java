    private void writeChannel(TGChannel channel) throws IOException {
        writeShort((short) channel.getChannelId());
        writeByte(channel.getBank());
        writeByte(channel.getProgram());
        writeByte(channel.getVolume());
        writeByte(channel.getBalance());
        writeByte(channel.getChorus());
        writeByte(channel.getReverb());
        writeByte(channel.getPhaser());
        writeByte(channel.getTremolo());
        writeUnsignedByteString(channel.getName());
        writeChannelParameters(channel);
    }
