    public int putMarker(EventMarker theMarker, String targetChannel) {
        try {
            try {
                this.channelId = this.cmap.Add(targetChannel);
                this.cmap.PutTimeAuto("timeofday");
                this.cmap.PutMime(this.channelId, EventMarker.MIME_TYPE);
            } catch (Exception e) {
                log.error("Error adding turbine channel: " + e.getMessage());
            }
            this.cmap.PutDataAsString(this.channelId, theMarker.toEventXmlString());
            this.source.Flush(this.cmap);
        } catch (Exception e) {
            log.error("Error writing to turbine: " + e);
        }
        return this.channelId;
    }
