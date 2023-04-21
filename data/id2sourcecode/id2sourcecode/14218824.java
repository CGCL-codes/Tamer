    private boolean exportAsText() throws CridmanagerException {
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsText() - start");
        }
        OutputStream out = null;
        String EPGtext = "";
        String channelName = "";
        String lineFeed = System.getProperty("line.separator");
        IFile destinationTXT = null;
        try {
            destinationTXT = path.create(fileName + ".txt");
            out = new BufferedOutputStream(destinationTXT.createOutput());
            if (settings.isSet(ISettings.SAVE_EPGINFO_TITLE)) {
                EPGtext += info.getCridTitel();
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_YEAR)) {
                if (EPGtext.length() != 0) EPGtext += lineFeed + lineFeed;
                EPGtext += info.getCridFilmTypeYear();
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_DESCRIPTION)) {
                if (EPGtext.length() != 0) EPGtext += lineFeed + lineFeed;
                EPGtext += info.getCridDescription();
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_BROADCASTSTATION)) {
                if (EPGtext.length() != 0) EPGtext += lineFeed + lineFeed;
                channelName = getChannelName();
                EPGtext += channelName;
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_DATE)) {
                if (settings.isSet(ISettings.SAVE_EPGINFO_BROADCASTSTATION)) EPGtext += ", "; else {
                    EPGtext += lineFeed + lineFeed;
                }
                EPGtext += date2shortString();
            }
            out.write(EPGtext.getBytes());
            out.flush();
        } catch (Exception e) {
            logger.error("exportAsText()", e);
            if (e instanceof FileNotFoundException) {
                errorTxt = Messages.getString("SaveEPGInfo.CantCreateFile");
            } else {
                errorTxt = e.getMessage();
            }
            String dest = destination == null ? Messages.getString("Global.Unbekannt") : destination.getAbsolutePath();
            String crid = info == null ? Messages.getString("Global.Unbekannt") : info.getFileName();
            throw new CridmanagerException(new StringBuffer(Messages.getString("SaveEPGInfo.SaveErrorMessage") + ", dest= " + dest + ", crid= " + crid + "). "), e);
        } finally {
            if (out != null) destinationTXT.close();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsText() - end");
        }
        return true;
    }
