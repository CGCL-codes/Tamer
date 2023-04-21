    void onWriteableEvent() throws IOException {
        assert (IoSocketDispatcher.isDispatcherThread());
        if (suspendRead) {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("[" + getId() + "] writeable event occured. update interested to none (because suspendRead is set) and write data to socket");
            }
            updateInterestedSetNonen();
        } else {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("[" + getId() + "] writeable event occured. update interested to read and write data to socket");
            }
            updateInterestedSetRead();
        }
        try {
            writeSendQueueDataToSocket();
            getPreviousCallback().onWritten();
        } catch (IOException ioe) {
            getPreviousCallback().onWriteException(ioe);
        }
        if (getSendQueueSize() > 0) {
            getDispatcher().updateInterestSet(this, SelectionKey.OP_WRITE);
        } else {
            if (shouldClosedPhysically()) {
                realClose();
            }
        }
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("[" + getId() + "] writeable event handled");
        }
    }
