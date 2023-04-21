    public void acceptConnections() throws IOException, InterruptedException {
        SelectionKey acceptKey = this.selectableChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        log.debug("Acceptor loop...");
        while ((this.keysAdded = acceptKey.selector().select()) > 0) {
            Set readyKeys = this.selector.selectedKeys();
            Iterator i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey key = (SelectionKey) i.next();
                i.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel nextReady = (ServerSocketChannel) key.channel();
                    log.debug("Processing selection key read=" + key.isReadable() + " write=" + key.isWritable() + " accept=" + key.isAcceptable());
                    SocketChannel channel = nextReady.accept();
                    channel.configureBlocking(false);
                    SelectionKey readKey = channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    readKey.attach(new ChannelCallback(channel));
                } else if (key.isReadable()) {
                    SelectableChannel nextReady = (SelectableChannel) key.channel();
                    log.debug("Processing selection key read=" + key.isReadable() + " write=" + key.isWritable() + " accept=" + key.isAcceptable());
                    this.readMessage((ChannelCallback) key.attachment());
                } else if (key.isWritable()) {
                }
            }
        }
        log.debug("End acceptor loop...");
    }
