    @Override
    public List<LogEvent> parse() throws IOException {
        long start = System.currentTimeMillis();
        ProgressIndicatorNeutral myProgressMonitor = getProgressMonitor();
        List<LogEvent> l = super.parse(new InputStreamReader(url.openStream()));
        myProgressMonitor.worked(toBeWorked);
        long end = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("parsed [" + url + "] in " + (end - start) + " milliseconds");
        }
        return l;
    }
