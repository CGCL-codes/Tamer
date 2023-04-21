    public void startServer() throws Exception {
        int channels = 0;
        int nKeys = 0;
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 9000);
        ssc.socket().bind(address);
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        SelectionKey s = ssc.register(selector, SelectionKey.OP_ACCEPT);
        printKeyInfo(s);
        while (true) {
            debug("NBTest: Starting select");
            nKeys = selector.select();
            if (nKeys > 0) {
                debug("NBTest: Number of keys after select operation: " + nKeys);
                Set<?> selectedKeys = selector.selectedKeys();
                Iterator<?> i = selectedKeys.iterator();
                while (i.hasNext()) {
                    s = (SelectionKey) i.next();
                    printKeyInfo(s);
                    debug("NBTest: Nr Keys in selector: " + selector.keys().size());
                    i.remove();
                    if (s.isAcceptable()) {
                        Socket socket = ((ServerSocketChannel) s.channel()).accept().socket();
                        SocketChannel sc = socket.getChannel();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println(++channels);
                    } else {
                        debug("NBTest: Channel not acceptable");
                    }
                }
            } else {
                debug("NBTest: Select finished without any keys.");
            }
        }
    }
