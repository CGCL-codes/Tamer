    public void run() {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            serverSocket = ssc.socket();
            InetSocketAddress isa = new InetSocketAddress(Constants.STANDARD_PORT);
            serverSocket.bind(isa);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            log.info("Connection listener started!");
            while (true) {
                if (threadSuspended) {
                    log.debug("Waiting for thread to be resumed.");
                    synchronized (this) {
                        wait();
                    }
                } else {
                    log.debug("Waiting for channel selections.");
                    int num = selector.select();
                    if (num == 0) {
                        continue;
                    }
                    log.debug("Channel selections occured.");
                    Set keys = selector.selectedKeys();
                    Iterator it = keys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = serverSocket.accept().getChannel();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            connectionManager.createConnection(sc);
                        } else if (key.isReadable()) {
                            receiveMessage((SocketChannel) key.channel());
                        }
                    }
                    keys.clear();
                }
            }
        } catch (IOException ioe) {
            log.error("Error during chanmel operation. Reason: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            log.error("Error in waiting for thread execution. Reason: " + ie.getMessage());
        }
    }
