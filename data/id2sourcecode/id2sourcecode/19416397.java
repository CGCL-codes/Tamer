    public BufferedTextSocket(final SocketChannel sock, final int readBufSizeVal, final int writeBufSizeVal) throws IOException {
        this(readBufSizeVal, writeBufSizeVal);
        attach(sock);
    }
