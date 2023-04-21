    private boolean loadStrips(final InputStream stripFileStream, final HashMap<String, Template> templateMap) {
        this.stripHandler = new StripHandler(stripFileStream, templateMap);
        this.channelStripMap = this.stripHandler.getChannelToStripMap();
        Iterator<Strip> it = stripHandler.getStripList().iterator();
        final SplitAxis stripAxis = lrd.stripAxis;
        while (it.hasNext()) {
            Strip p = it.next();
            addStrip(p, stripAxis);
        }
        LiveRAC.makeLogEntry(LogEntry.STRIP_LOAD_TYPE, "Load strips from server.", stripHandler.getStripList());
        return true;
    }
