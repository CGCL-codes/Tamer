    private void onWriteableEvent(AbstractChannelBasedEndpoint handler) {
        try {
            handler.onWriteableEvent();
        } catch (Exception e) {
            LOG.warning("[" + Thread.currentThread().getName() + "] exception occured while handling writeable event. Reason " + DataConverter.toString(e));
        }
        handledWrites++;
    }
