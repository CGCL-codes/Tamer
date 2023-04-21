    public void run() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.config("Listening on port " + port);
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (true) {
                try {
                    if (selector.select() > 0) {
                        Iterator it = selector.selectedKeys().iterator();
                        while (it.hasNext()) {
                            SelectionKey rsk = (SelectionKey) it.next();
                            it.remove();
                            int rskOps = rsk.readyOps();
                            if ((rskOps & SelectionKey.OP_ACCEPT) > 0) {
                                Socket socket = ((ServerSocketChannel) rsk.channel()).accept().socket();
                                logger.fine("Accepted connection: host='" + socket.getInetAddress().getHostName() + "', port=" + socket.getPort() + ", IP=" + socket.getInetAddress().getHostAddress());
                                talker.processSystemMessage("accept: " + socket.getInetAddress().getHostName());
                                TCPUser u = new TCPUser(socket, this, talker);
                                socketUser.put(socket, u);
                                SocketChannel sc = socket.getChannel();
                                sc.configureBlocking(false);
                                sc.register(selector, SelectionKey.OP_READ);
                                u.handshake();
                            } else if ((rskOps & SelectionKey.OP_READ) > 0) {
                                read(rsk, buffer);
                            }
                        }
                    }
                } catch (IOException e) {
                    if (e.getMessage().equals("Interrupted system call")) {
                        logger.fine("Interrupted select(). OK...");
                    } else {
                        Utils.unexpectedExceptionWarning(e);
                    }
                } catch (CallbackException e) {
                    Utils.unexpectedExceptionWarning(e);
                } catch (Exception e) {
                    Utils.unexpectedExceptionWarning(e);
                }
                if (shutdown) {
                    try {
                        logger.severe("TCPAdapter shutdown... OK...");
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            selector.close();
        } catch (IOException e) {
            Utils.unexpectedExceptionWarning(e);
        } catch (Throwable t) {
            Utils.unexpectedExceptionWarning(t);
            try {
                shutdown();
            } catch (IOException e) {
                Utils.unexpectedExceptionWarning(e);
            }
        }
    }
