    public void addLines() {
        Logger.getLogger("map").info("adding lines for channels");
        for (Channel data : getChannels().getChannels()) {
            addPolylineForChannel(data);
        }
    }
