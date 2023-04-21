    @Override
    protected void shutdownImpl() {
        Logger.info("Shutting down " + this);
        try {
            readThread.kill();
        } catch (InterruptedException e) {
            Logger.error("StreamConnection interrupted while shutting down read thread", e);
        }
        try {
            writeThread.kill();
        } catch (InterruptedException e) {
            Logger.error("StreamConnection interrupted while shutting down write thread", e);
        }
        try {
            out.flush();
        } catch (IOException e) {
            Logger.error("StreamConnection error flushing output buffer", e);
        }
        try {
            out.close();
        } catch (IOException e) {
            Logger.error("StreamConnection error closing output buffer", e);
        }
        try {
            in.close();
        } catch (IOException e) {
            Logger.error("StreamConnection error closing input buffer", e);
        }
    }
