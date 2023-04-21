    @Override
    public byte[] read() throws IOException {
        SocketChannel sc = this.in.getChannel();
        byte[] incoming = {};
        ByteBuffer tmp = ByteBuffer.wrap(new byte[1024]);
        tmp.rewind();
        int l = 0;
        while ((l = sc.read(tmp)) >= 0) {
            tmp.flip();
            incoming = this.concatArray(incoming, tmp.array(), l);
            tmp.clear();
        }
        sc.close();
        this.in.close();
        new ByteTransferredEvent(incoming.length, true).publish();
        return incoming;
    }
