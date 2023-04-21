    private boolean checkMediaCast(WeblogEntryData entry, ActionMessages uiMessages) {
        boolean valid = false;
        String url = entry.findEntryAttribute("att_mediacast_url");
        boolean empty = (url == null) || (url.trim().length() == 0);
        if (!empty) {
            valid = false;
            try {
                mLogger.debug("Sending HTTP HEAD");
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                mLogger.debug("Getting response code");
                con.setRequestMethod("HEAD");
                if (con.getResponseCode() != 200) {
                    mLogger.debug("Response code indicates error");
                    mLogger.error("ERROR " + con.getResponseCode() + " return from MediaCast URL");
                    uiMessages.add(null, new ActionMessage("weblogEdit.mediaCastResponseError"));
                } else if (con.getContentType() == null || con.getContentLength() == -1) {
                    mLogger.debug("Content type + (" + con.getContentType() + " is null or content length (" + con.getContentLength() + ") is -1 (indeterminate).");
                    uiMessages.add(null, new ActionMessage("weblogEdit.mediaCastLacksContentTypeOrLength"));
                } else {
                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug("Got good response: Content type " + con.getContentType() + " [length = " + con.getContentLength() + "]");
                    }
                    entry.putEntryAttribute("att_mediacast_type", con.getContentType());
                    entry.putEntryAttribute("att_mediacast_length", "" + con.getContentLength());
                    valid = true;
                }
            } catch (MalformedURLException mfue) {
                mLogger.debug("Malformed MediaCast url: " + url);
                uiMessages.add(null, new ActionMessage("weblogEdit.mediaCastUrlMalformed"));
            } catch (Exception e) {
                mLogger.error("ERROR while checking MediaCast URL: " + url + ": " + e.getMessage());
                uiMessages.add(null, new ActionMessage("weblogEdit.mediaCastFailedFetchingInfo"));
            }
        } else {
            mLogger.debug("No MediaCast specified, but that is OK");
            valid = true;
        }
        if (!valid || empty) {
            mLogger.debug("Removing MediaCast attributes");
            try {
                entry.removeEntryAttribute("att_mediacast_url");
                entry.removeEntryAttribute("att_mediacast_type");
                entry.removeEntryAttribute("att_mediacast_length");
            } catch (RollerException e) {
                mLogger.error("ERROR removing invalid MediaCast attributes");
            }
        }
        mLogger.debug("operation complete");
        return valid;
    }
