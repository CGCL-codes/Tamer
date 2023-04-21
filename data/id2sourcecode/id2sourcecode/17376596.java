    public void close(int id) {
        final AssertRpsCoordinatorSessionHandler handler = getHandler(id);
        try {
            handler.close();
        } catch (IOException ioe) {
            LOG.warn(ioe.getMessage(), ioe);
        }
        handlerMap.remove(handler);
        channelMap.remove(getChannel(id));
    }
