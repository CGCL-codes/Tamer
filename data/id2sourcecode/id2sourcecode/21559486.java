    private void processSocketEvents() {
        try {
            if (sockSelector.select(5000) > 0) {
                Set<SelectionKey> keys = sockSelector.selectedKeys();
                Iterator<SelectionKey> i = keys.iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        Peer peer = (Peer) key.attachment();
                        try {
                            if (channel.isConnectionPending()) {
                                channel.finishConnect();
                                PeerConnection conn = new PeerConnection(peer, channel, metadata.getInfoHash());
                                conn.enqueue(new PendingHandshake(metadata.getInfoHash(), ConfigManager.getClientId().getBytes()));
                                connectedPeers.add(conn);
                                key.attach(conn);
                                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                                logger.fine("Connected to new peer: " + peer);
                            }
                        } catch (Exception e) {
                            logger.fine("Can not connect to peer " + peer + ". " + e.getMessage());
                            key.attach(null);
                            key.cancel();
                        }
                    }
                    if (key.isValid() && key.isWritable()) {
                        PeerConnection conn = (PeerConnection) key.attachment();
                        try {
                            if (!conn.doWrite()) {
                                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                            }
                        } catch (Exception e) {
                            logger.fine("Error sending message to " + conn.getPeer() + ". Closing connection: " + e.getMessage());
                            conn.kill();
                            key.attach(null);
                            key.cancel();
                            connectedPeers.remove(conn);
                        }
                    }
                    if (key.isValid() && key.isReadable()) {
                        PeerConnection conn = (PeerConnection) key.attachment();
                        try {
                            if (!conn.getChannel().isOpen()) {
                                logger.fine("Socket closed. Connection with " + conn.getPeer() + " dropped");
                                conn.kill();
                                key.cancel();
                            } else {
                                conn.doRead();
                            }
                        } catch (Exception e) {
                            logger.fine("Error reading from socket (" + e.getMessage() + "). Closing connection with " + conn.getPeer());
                            conn.kill();
                            key.attach(null);
                            key.cancel();
                            connectedPeers.remove(conn);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
