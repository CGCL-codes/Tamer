    private void broadcastData(final byte[] dataToBroadcast) {
        if (this.selector != null) try {
            synchronized (this.pendingData) {
                Set<Socket> channels = this.pendingData.keySet();
                for (Socket socket : channels) {
                    SelectionKey selectionKey = socket.getChannel().keyFor(this.selector);
                    LinkedList<ByteBuffer> queue = this.pendingData.get(socket);
                    if (queue.size() < MAXQUEUELENGTH) {
                        queue.addLast(ByteBuffer.wrap(dataToBroadcast));
                    }
                    if (selectionKey != null && selectionKey.channel() != null) {
                        selectionKey.interestOps((SelectionKey.OP_READ | SelectionKey.OP_WRITE) & selectionKey.channel().validOps());
                    }
                }
            }
            this.selector.wakeup();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error broadcasting some data", e);
        }
    }
