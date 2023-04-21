    private boolean exportAsCSV_DB() throws CridmanagerException {
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsCSV_DB() - start");
        }
        String wholeText = "";
        ;
        String quoteSign = "\"";
        String channelName = "";
        String lineFeed = System.getProperty("line.separator");
        String databaseName = Messages.getString("SaveEPGInfo.DatabaseFile");
        OutputStream out = null;
        FsFile destinationCSV_DB = null;
        try {
            destinationCSV_DB = new FsFile(Utils.getUserHome(databaseName));
            if (!destinationCSV_DB.exists()) {
                out = new BufferedOutputStream(destinationCSV_DB.createOutput());
            } else {
                out = new BufferedOutputStream(destinationCSV_DB.getOutput());
            }
            wholeText = quoteSign + info.getCridTitel().replace('\"', '\'') + quoteSign;
            wholeText += ";";
            wholeText += quoteSign + info.getCridFilmTypeYear().replace('\"', '\'') + quoteSign;
            wholeText += ";";
            wholeText += quoteSign + info.getCridDescription().replace('\"', '\'') + quoteSign;
            wholeText += ";";
            channelName = getChannelName();
            wholeText += quoteSign + channelName.replace('\"', '\'') + quoteSign;
            wholeText += ";";
            wholeText += quoteSign + date2shortString() + quoteSign;
            wholeText += lineFeed;
            out.write(wholeText.getBytes());
            out.flush();
        } catch (Exception e) {
            logger.error("exportAsCSV_DB()", e);
            if (e instanceof FileNotFoundException) {
                errorTxt = Messages.getString("SaveEPGInfo.CantCreateFile");
            } else {
                errorTxt = e.getMessage();
            }
            String dest = destination == null ? Messages.getString("Global.Unbekannt") : destination.getAbsolutePath();
            String crid = info == null ? Messages.getString("Global.Unbekannt") : info.getFileName();
            throw new CridmanagerException(new StringBuffer(Messages.getString("SaveEPGInfo.SaveErrorMessage") + ", dest= " + dest + ", crid= " + crid + "). "), e);
        } finally {
            if (out != null) destinationCSV_DB.close();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsCSV_DB() - end");
        }
        return true;
    }
