    private synchronized InputStream getNextTrack() {
        String request = null;
        int trackBitrate = 0;
        boolean trackVBR = false;
        String trackTitle = null;
        String trackArtist = null;
        String htmlSafeTrackTitle = null;
        String htmlSafeTrackArtist = null;
        URL url = null;
        InputStream retVal = null;
        MPEGStream durationStream = null;
        badBitrate = false;
        trackPosition = 0;
        trackPlayingTime = 0;
        if (nextTrack == null) {
            nextTrack = playlist.getNextTrack();
        }
        if (nextTrack != null) {
            playing = nextTrack;
            nextTrack = null;
            trackTitle = playing.getAttributeValue("Title").getString();
            trackArtist = playing.getAttributeValue("Artist").getString();
            try {
                htmlSafeTrackTitle = fixEncoding(URLEncoder.encode(trackTitle, "UTF-8"));
                htmlSafeTrackArtist = fixEncoding(URLEncoder.encode(trackArtist, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.warn("Could not encode track details", e);
                htmlSafeTrackTitle = "borken";
                htmlSafeTrackArtist = "borken";
            }
            if (htmlSafeTrackTitle.equals("")) {
                htmlSafeTrackTitle = "NONE SPECIFIED";
            }
            if (htmlSafeTrackArtist.equals("")) {
                htmlSafeTrackArtist = "NONE SPECIFIED";
            }
            trackBitrate = playing.getAttributeValue("Bitrate").getInt();
            trackVBR = (playing.getAttributeValue("VBR").getInt() == 1) ? true : false;
            if (trackBitrate > 448 || trackBitrate < 32) {
                log.warn("Track: " + trackArtist + " - " + trackTitle + "BAD BITRATE: " + trackBitrate);
                badBitrate = true;
            }
            if (logInfoEnabled) log.info("+++ playing track " + trackTitle + "(" + trackBitrate + " kbps)" + (trackVBR ? " VBR" : ""));
            url = playing.getURL();
            if (logDebugEnabled) log.debug("+++ reading from URL: " + url.toString());
            try {
                retVal = url.openStream();
                durationStream = new MPEGStream(retVal);
                trackPlayingTime = durationStream.getPlayingTime();
                durationStream.close();
                durationStream = null;
                retVal = url.openStream();
                request = outputTrackInfo(stream.getPassword(), htmlSafeTrackArtist, htmlSafeTrackTitle, stream.getURL(), stream.getServerName(), stream.getTrackInfoPort());
                if (logDebugEnabled) log.debug("+++ sent track info request: " + request);
                if (logDebugEnabled) log.debug(retVal.available() + " bytes in file");
            } catch (FileNotFoundException e) {
                log.warn("Could not find file " + url.toString());
            } catch (IOException ioe) {
                log.warn("Exception getting stream from track URL", ioe);
            }
        }
        return retVal;
    }
