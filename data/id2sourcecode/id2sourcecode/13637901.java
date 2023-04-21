    public void handleNetwork() {
        while (true) {
            try {
                int num = connectionsSelector.select();
                if (num == 0) {
                    continue;
                }
                Set<SelectionKey> activeConnections = connectionsSelector.selectedKeys();
                Iterator<SelectionKey> activeConnectionsIter = activeConnections.iterator();
                while (activeConnectionsIter.hasNext()) {
                    SelectionKey key = activeConnectionsIter.next();
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        Socket s = acceptSocket.accept();
                        SocketChannel sc = s.getChannel();
                        sc.configureBlocking(false);
                        sc.register(connectionsSelector, SelectionKey.OP_READ);
                        sc.finishConnect();
                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (!handleReadOp(channel)) {
                            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Closing connection with client '" + channel.socket().getInetAddress() + "'");
                            key.cancel();
                            try {
                                if (channel.isOpen()) {
                                    channel.close();
                                }
                            } catch (IOException closeEx) {
                                Logger.getLogger(this.getClass().getName()).logp(Level.WARNING, this.getClass().getName(), "handleNetwork", "Network error - failed to close connection", closeEx);
                            }
                        }
                    }
                }
                activeConnections.clear();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(), "handleNetwork", "Network error", ex);
            }
        }
    }
