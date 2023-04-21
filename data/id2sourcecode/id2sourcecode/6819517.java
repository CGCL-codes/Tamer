    public void destroy() {
        if (null != writeThread) {
            try {
                writeThread.interrupt();
            } catch (Exception e) {
                log.error("interrupt write thread error", e);
            }
        }
    }
