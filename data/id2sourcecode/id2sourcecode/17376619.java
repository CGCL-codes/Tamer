    private void ensureMessage(int id) throws RpsSessionException {
        final AssertRpsCoordinatorSessionHandler handler = getHandler(id);
        if (handler.hasCachedMessage()) {
            LOG.debug("ensureMessage: message cached");
            return;
        }
        LOG.debug("ensureMessage: try to read message");
        Selector selector = null;
        try {
            selector = Selector.open();
            final SocketChannel channel = getChannel(id);
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            selector.select(selectTimeout);
            if (selector.selectedKeys().size() == 1) {
                LOG.debug("read for: " + id);
                if (handler.read() > 0) {
                    return;
                }
                close(id);
                throw new IllegalStateException();
            }
            close(id);
            throw new RpsCommunicationException(String.format("receiving timeout: %d [msec]", selectTimeout));
        } catch (IOException ioe) {
            LOG.warn(ioe.getMessage(), ioe);
            close(id);
            throw new RpsSessionException(ioe);
        } finally {
            try {
                selector.close();
            } catch (IOException ioe) {
                LOG.debug(ioe.getMessage(), ioe);
            }
        }
    }
