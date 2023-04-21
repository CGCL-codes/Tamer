    private boolean exportAsCSV() throws CridmanagerException {
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsCSV() - start");
        }
        OutputStream out = null;
        String wholeText = "";
        ;
        String quoteSign = "\"";
        String channelName = "";
        IFile destinationCSV = null;
        try {
            destinationCSV = path.create(fileName + ".csv");
            out = new BufferedOutputStream(destinationCSV.createOutput());
            if (settings.isSet(ISettings.SAVE_EPGINFO_TITLE)) {
                wholeText = quoteSign + info.getCridTitel().replace('\"', '\'') + quoteSign;
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_YEAR)) {
                if (wholeText.length() != 0) wholeText += ";";
                wholeText += quoteSign + info.getCridFilmTypeYear().replace('\"', '\'') + quoteSign;
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_DESCRIPTION)) {
                if (wholeText.length() != 0) wholeText += ";";
                wholeText += quoteSign + info.getCridDescription().replace('\"', '\'') + quoteSign;
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_BROADCASTSTATION)) {
                if (wholeText.length() != 0) wholeText += ";";
                channelName = getChannelName();
                wholeText += quoteSign + channelName.replace('\"', '\'') + quoteSign;
            }
            if (settings.isSet(ISettings.SAVE_EPGINFO_DATE)) {
                if (wholeText.length() != 0) wholeText += ";";
                wholeText += quoteSign + date2shortString() + quoteSign;
            }
            out.write(wholeText.getBytes());
            out.flush();
        } catch (Exception e) {
            logger.error("exportAsCSV()", e);
            if (e instanceof FileNotFoundException) {
                errorTxt = Messages.getString("SaveEPGInfo.CantCreateFile");
            } else {
                errorTxt = e.getMessage();
            }
            String dest = destination == null ? Messages.getString("Global.Unbekannt") : destination.getAbsolutePath();
            String crid = info == null ? Messages.getString("Global.Unbekannt") : info.getFileName();
            throw new CridmanagerException(new StringBuffer(Messages.getString("SaveEPGInfo.SaveErrorMessage") + ", dest= " + dest + ", crid= " + crid + "). "), e);
        } finally {
            if (out != null) destinationCSV.close();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsCSV() - end");
        }
        return true;
    }
