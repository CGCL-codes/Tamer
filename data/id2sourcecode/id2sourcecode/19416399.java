    public BufferedTextSocket(final Socket sock, final int readBufSizeVal, final int writeBufSizeVal) throws IOException {
        this((null == sock) ? null : sock.getChannel(), readBufSizeVal, writeBufSizeVal);
    }
