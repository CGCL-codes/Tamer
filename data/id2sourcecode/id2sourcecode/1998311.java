    public void start() throws Exception {
        SelectionKey acceptKey = this.selectableChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("Acceptor loop...");
        while ((keysAdded = acceptKey.selector().select()) > 0) {
            Set<SelectionKey> readyKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey key = (SelectionKey) i.next();
                i.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel nextReady = (ServerSocketChannel) key.channel();
                    SocketChannel channel = nextReady.accept();
                    channel.configureBlocking(false);
                    SelectionKey readKey = channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    readKey.attach(new ChannelCallback(channel));
                } else if (key.isReadable()) {
                    readMessage((ChannelCallback) key.attachment());
                } else if (key.isWritable()) {
                    ChannelCallback callback = (ChannelCallback) key.attachment();
                    byte[] somethingToWrite = getSomethingToWrite(callback);
                    if (somethingToWrite != null) {
                        ByteBuffer buf = ByteBuffer.wrap(somethingToWrite);
                        callback.getChannel().write(buf);
                    }
                }
            }
        }
        System.out.println("End acceptor loop...");
    }
