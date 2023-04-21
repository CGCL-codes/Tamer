    private void handleIO(SelectionKey sk) {
        MemcachedNode qa = (MemcachedNode) sk.attachment();
        try {
            getLogger().debug("Handling IO for:  %s (r=%s, w=%s, c=%s, op=%s)", sk, sk.isReadable(), sk.isWritable(), sk.isConnectable(), sk.attachment());
            if (sk.isConnectable()) {
                getLogger().info("Connection state changed for %s", sk);
                final SocketChannel channel = qa.getChannel();
                if (channel.finishConnect()) {
                    assert channel.isConnected() : "Not connected.";
                    qa.connected();
                    addedQueue.offer(qa);
                    if (qa.getWbuf().hasRemaining()) {
                        handleWrites(sk, qa);
                    }
                } else {
                    assert !channel.isConnected() : "connected";
                }
            } else {
                if (sk.isReadable()) {
                    handleReads(sk, qa);
                }
                if (sk.isWritable()) {
                    handleWrites(sk, qa);
                }
            }
        } catch (Exception e) {
            getLogger().info("Reconnecting due to exception on %s", qa, e);
            queueReconnect(qa);
        }
        qa.fixupOps();
    }
