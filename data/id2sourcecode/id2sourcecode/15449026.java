    @Override
    public final Integer call() throws java.io.IOException {
        Iterator<SelectionKey> it = null;
        SelectionKey selKey = null;
        if (writerThread == null) {
            writerThread = Thread.currentThread();
        }
        while (true) {
            selector.select(WRITE_EVENT_WAIT_INTERVAL);
            registerClients();
            unregisterClients();
            it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                selKey = it.next();
                it.remove();
                if (selKey.isWritable()) {
                    try {
                        write(selKey);
                    } catch (final WriterException e) {
                        logger.warn("unable to write message", e);
                    }
                }
            }
            if (totalMessageCount == 0) {
                try {
                    Thread.sleep(WAIT_FOR_NEW_MESSAGES_INTERVAL);
                } catch (final InterruptedException e) {
                }
            }
        }
    }
