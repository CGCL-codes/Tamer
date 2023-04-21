    public void run() {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ServerSocket ss = ssc.socket();
            InetSocketAddress isa = new InetSocketAddress(port);
            ss.bind(isa);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Listening on port " + port);
            while (true) {
                int num = selector.select();
                if (num == 0) {
                    continue;
                }
                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        System.out.println("acc");
                        Socket s = ss.accept();
                        System.out.println("Got connection from " + s);
                        SocketChannel sc = s.getChannel();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        SocketChannel sc = null;
                        try {
                            sc = (SocketChannel) key.channel();
                            boolean ok = processInput(sc);
                            if (!ok) {
                                key.cancel();
                                Socket s = null;
                                try {
                                    s = sc.socket();
                                    s.close();
                                } catch (IOException ie) {
                                    System.err.println("Error closing socket " + s + ": " + ie);
                                }
                            }
                        } catch (IOException ie) {
                            key.cancel();
                            try {
                                sc.close();
                            } catch (IOException ie2) {
                                System.out.println(ie2);
                            }
                            System.out.println("Closed " + sc);
                        }
                    }
                }
                keys.clear();
            }
        } catch (IOException ie) {
            System.err.println(ie);
        }
    }
