    protected boolean registerConnection(Connection connection, int key) {
        SocketChannel channel = connection.getChannel();
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getName() + "] registed Connection[" + connection.toString() + "] connected!");
        }
        SelectionKey selkey = null;
        try {
            if (!(channel instanceof SelectableChannel)) {
                try {
                    logger.warn("Provided with un-selectable socket as result of accept(), can't " + "cope [channel=" + channel + "].");
                } catch (Error err) {
                    logger.warn("Un-selectable channel also couldn't be printed.");
                }
                if (channel != null) {
                    channel.socket().close();
                }
                return false;
            }
            SelectableChannel selchan = (SelectableChannel) channel;
            selchan.configureBlocking(false);
            selkey = selchan.register(_selector, key, connection);
            connection.setConnectionManager(this);
            connection.setSelectionKey(selkey);
            _stats.connects.incrementAndGet();
            connection.init();
            _selector.wakeup();
            return true;
        } catch (IOException ioe) {
            logger.error("register connection error: " + ioe);
        }
        if (selkey != null) {
            selkey.attach(null);
            selkey.cancel();
        }
        if (channel != null) {
            try {
                channel.socket().close();
            } catch (IOException ioe) {
                logger.warn("Failed closing aborted connection: " + ioe);
            }
        }
        return false;
    }
