    public void run() {
        debug.enter(DUTL, this, "run()");
        try {
            setProcessingStatus(PROC_RECEIVING);
            while (keepProcessing) {
                process();
                Thread.yield();
            }
        } catch (Exception e) {
            setTermException(e);
            debug.write("ProcessingThread.run() caught unhadled exception " + e);
            event.write(e, "ProcessingThread.run() unhadled exception");
        } finally {
            setProcessingStatus(PROC_FINISHED);
            debug.exit(DUTL, this);
        }
    }
