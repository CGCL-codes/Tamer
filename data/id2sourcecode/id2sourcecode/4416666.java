    private void attemptReconnects() throws IOException {
        final long now = System.currentTimeMillis();
        final Map<MemcachedNode, Boolean> seen = new IdentityHashMap<MemcachedNode, Boolean>();
        for (Iterator<MemcachedNode> i = reconnectQueue.headMap(now).values().iterator(); i.hasNext(); ) {
            final MemcachedNode qa = i.next();
            i.remove();
            if (!seen.containsKey(qa)) {
                seen.put(qa, Boolean.TRUE);
                getLogger().info("Reconnecting %s", qa);
                final SocketChannel ch = SocketChannel.open();
                ch.configureBlocking(false);
                int ops = 0;
                if (ch.connect(qa.getSocketAddress())) {
                    getLogger().info("Immediately reconnected to %s", qa);
                    assert ch.isConnected();
                } else {
                    ops = SelectionKey.OP_CONNECT;
                }
                qa.registerChannel(ch, ch.register(selector, ops, qa));
                assert qa.getChannel() == ch : "Channel was lost.";
            } else {
                getLogger().debug("Skipping duplicate reconnect request for %s", qa);
            }
        }
    }
