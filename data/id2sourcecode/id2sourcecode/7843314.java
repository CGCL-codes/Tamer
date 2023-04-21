    public int putTempMarker(String theMarker, String targetChannel) {
        try {
            log.debug("Putting marker\n" + theMarker + "\ninto channel \"" + targetChannel + "\"" + " on " + serverName);
            try {
                this.channelId = this.cmap.Add(targetChannel);
                this.cmap.PutTimeAuto("timeofday");
                this.cmap.PutMime(this.channelId, EventMarker.MIME_TYPE);
            } catch (Exception e) {
                log.error("Error adding turbine channel: " + e);
            }
            this.cmap.PutDataAsString(this.channelId, theMarker);
            this.source.Flush(this.cmap);
        } catch (Exception e) {
            log.error("Error writing to turbine: " + e);
        }
        return this.channelId;
    }
