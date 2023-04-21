    @SuppressWarnings("unchecked")
    public final void run() {
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("selector  listening ...");
        }
        while (isOpen) {
            try {
                synchronized (dispatcherThreadGuard) {
                }
                int eventCount = 0;
                try {
                    eventCount = selector.select(1000);
                } catch (Throwable e) {
                    LOG.warning("sync exception occured while processing. Reason " + e.toString());
                }
                if (eventCount > 0) {
                    Set selectedEventKeys = selector.selectedKeys();
                    Iterator it = selectedEventKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey eventKey = (SelectionKey) it.next();
                        it.remove();
                        T handle = (T) eventKey.attachment();
                        if (eventKey.isValid() && eventKey.isReadable()) {
                            try {
                                eventHandler.onHandleReadableEvent(handle);
                            } catch (Throwable e) {
                                LOG.warning("[" + Thread.currentThread().getName() + "] exception occured while handling readable event. Reason " + e.toString());
                            }
                            handledReads++;
                        }
                        if (eventKey.isValid() && eventKey.isWritable()) {
                            handledWrites++;
                            try {
                                eventHandler.onHandleWriteableEvent(handle);
                            } catch (Throwable e) {
                                LOG.warning("[" + Thread.currentThread().getName() + "] exception occured while handling writeable event. Reason " + e.toString());
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                LOG.warning("[" + Thread.currentThread().getName() + "] exception occured while processing. Reason " + e.toString());
            }
        }
        closeDispatcher();
    }
