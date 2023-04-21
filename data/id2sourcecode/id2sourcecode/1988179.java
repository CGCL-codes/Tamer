    @Override
    public void write(OutputStream out) throws IOException {
        SSHProtocol.writeUint32(out, getChannelNumber());
        SSHProtocol.writeStringLength(out, getDataLen());
    }
