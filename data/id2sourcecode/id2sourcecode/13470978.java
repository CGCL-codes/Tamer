    public void run() {
        try {
            while (istream.getChannel().isOpen()) {
                try {
                    final FIXMessage m = readMessage();
                    try {
                        if (m != null && filter.filter(m.getMsgType())) {
                            messages.put(m);
                        }
                    } catch (HermesRuntimeException ex) {
                        log.error("ignoring invalid message: " + ex.getMessage(), ex);
                    }
                } catch (BufferUnderflowException ex) {
                    waitAndRemap();
                } catch (IllegalArgumentException ex) {
                    waitAndRemap();
                }
            }
            log.debug("channel closed");
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
        }
    }
