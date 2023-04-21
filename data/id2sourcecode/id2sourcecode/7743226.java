    public void close() {
        Object t = getSelectorManager().getThread();
        if (t != null && Thread.currentThread().equals(t)) {
            throw new RuntimeException(this + "You should not perform a blocking close " + "on the channelmanager thread for performance reasons.  Use the cm threading layer, " + "or put the code calling this write on another thread");
        }
        try {
            UtilWaitForCompletion waitWrite = new UtilWaitForCompletion(this, null);
            close(waitWrite, -1);
            waitWrite.waitForComplete();
        } catch (Exception e) {
            log.log(Level.WARNING, this + "Exception closing channel", e);
        }
    }
