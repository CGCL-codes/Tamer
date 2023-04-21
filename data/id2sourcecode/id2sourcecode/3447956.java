    @Override
    public void encode(SSHOutputDataEncoder out) throws IOException {
        if (null == out) throw new IOException("encode(" + getMsgCode() + ") no " + SSHOutputDataEncoder.class.getSimpleName() + " instance");
        out.writeASCII(getChannelType());
        out.writeInt(getSenderChannel());
        out.writeInt(getInitialWindowSize());
        out.writeInt(getMaxPacketSize());
    }
