    public final void grab() {
        queue = new ArrayBlockingQueue(getChannels().size() * daysToGrab);
        XMLTVGrabberThreadPool threadPool = new XMLTVGrabberThreadPool(MAX_THREADS, MAX_THREADS, TIMEOUT, TimeUnit.SECONDS, queue, getDaysToGrab(), getChannels(), getFactory());
        TvDocument doc = threadPool.grab();
        try {
            if (log.isDebugEnabled()) {
                log.debug(doc.toString());
            }
            if (getExport() != null) {
                getExport().export(doc);
            }
        } catch (ExportException e) {
            if (log.isErrorEnabled()) {
                log.error("Unable to export document", e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("ALL DONE");
        }
    }
