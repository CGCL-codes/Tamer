    @Override
    public void write(OutputStream out) throws IOException {
        SSHProtocol.writeStringBytes(out, true, getChannelType());
        SSHProtocol.writeUint32(out, getSenderChannel());
        SSHProtocol.writeUint32(out, getInitialWindowSize());
        SSHProtocol.writeUint32(out, getMaxPacketSize());
    }
