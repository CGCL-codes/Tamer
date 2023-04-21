    public ParallelChannel(Reader reader, Writer writer, InetAddress address, int port) throws IOException {
        this.reader = reader;
        this.writer = writer;
        SocketAddress sa = new InetSocketAddress(address, port);
        channel = SocketChannel.open(sa);
        channel.socket().setReceiveBufferSize(channel.socket().getReceiveBufferSize() * 4);
        if (ProtocolV2.isEncryptedPort(port)) {
            channel.configureBlocking(true);
            SymmetricKey key = SymmetricKey.createNew();
            ((WriterV2) writer).initEncryption(key);
            ((ReaderV2) reader).initEncryption(key);
            StartEncryptionPing ping = new StartEncryptionPing(key, CONNECTION_PASSWORD);
            ByteBuffer header = ping.getHeader();
            ByteBuffer init = ping.getBytes();
            Util.writeFully(channel, header);
            Util.writeFully(channel, init);
        } else {
            ((WriterV2) writer).initEncryption(null);
            ((ReaderV2) reader).initEncryption(null);
        }
        channel.configureBlocking(false);
        selector = Selector.open();
        key = channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
    }
