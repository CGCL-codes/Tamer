    @Override
    public void encode(SSHOutputDataEncoder out) throws IOException {
        if (null == out) throw new IOException("encode(" + getMsgCode() + ") no " + SSHOutputDataEncoder.class.getSimpleName() + " instance");
        out.writeInt(getChannelNumber());
        out.writeInt(getDataLen());
    }
