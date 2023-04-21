    public boolean readConfig() {
        boolean goodRead = true;
        int x, y;
        int width, height;
        ButIniProp p = new ButIniProp();
        try {
            p.load(new FileInputStream(INI_FILENAME));
        } catch (IOException exc) {
            ConsoleDialog.writeError("Your " + INI_FILENAME + " file cannot" + " be found," + "\nyour settings could not be read.", exc);
            goodRead = false;
        }
        if (null == myDriver.setBitPath(p.getProperty(p.bitPath))) {
            myDriver.setBitPath(Driver.DEFAULT_BT_PATH);
            goodRead = false;
        }
        if (null == myDriver.setDownloadFolder(p.getProperty(p.downloadFolder))) {
            myDriver.setDownloadFolder(Driver.DEFAULT_DOWNLOAD_FOLDER);
            goodRead = false;
        }
        myDriver.setChangedDownloadLocation(!myDriver.getDownloadFolder().equals(Driver.DEFAULT_DOWNLOAD_FOLDER));
        if (null == myDriver.setAltDownloaderPath(p.getProperty(p.altDownloader))) {
            myDriver.setAltDownloaderPath(Driver.DEFAULT_ALT_DOWNLOADER);
            goodRead = false;
        }
        if (null == p.getProperty(p.lastUpdate)) {
            UpdateChecker.setLastUpdated(UpdateChecker.DEFAULT_UPDATE);
        } else {
            UpdateChecker.setLastUpdated(new Date(new Long(p.getProperty(p.lastUpdate)).longValue()));
        }
        try {
            myDriver.setTimeDelay(Integer.parseInt(p.getProperty(p.delay)));
        } catch (NumberFormatException exc) {
            myDriver.setTimeDelay(Driver.MIN_DELAY);
            goodRead = false;
        } catch (NullPointerException exc) {
            myDriver.setTimeDelay(Driver.MIN_DELAY);
            goodRead = false;
        }
        try {
            x = Integer.parseInt(p.getProperty(p.locX));
        } catch (NumberFormatException exc) {
            x = Gui.DEFAULT_X_LOC;
            goodRead = false;
        } catch (NullPointerException exc) {
            x = Gui.DEFAULT_X_LOC;
            goodRead = false;
        }
        try {
            y = Integer.parseInt(p.getProperty(p.locY));
        } catch (NumberFormatException exc) {
            y = Gui.DEFAULT_Y_LOC;
            goodRead = false;
        } catch (NullPointerException exc) {
            y = Gui.DEFAULT_Y_LOC;
            goodRead = false;
        }
        try {
            width = Integer.parseInt(p.getProperty(p.width));
        } catch (NumberFormatException exc) {
            width = Gui.DEFAULT_WIDTH;
            goodRead = false;
        } catch (NullPointerException exc) {
            width = Gui.DEFAULT_WIDTH;
            goodRead = false;
        }
        try {
            height = Integer.parseInt(p.getProperty(p.height));
        } catch (NumberFormatException exc) {
            height = Gui.DEFAULT_HEIGHT;
            goodRead = false;
        } catch (NullPointerException exc) {
            height = Gui.DEFAULT_HEIGHT;
            goodRead = false;
        }
        try {
            myDriver.setAutoStart(new Boolean(p.getProperty(p.start)).booleanValue());
        } catch (NullPointerException exc) {
            myDriver.setAutoStart(Driver.DEFAULT_USE_AUTO_START);
            goodRead = false;
        }
        try {
            Email.setSendEmail(new Boolean(p.getProperty(p.sendEmail)).booleanValue());
        } catch (NullPointerException exc) {
            Email.setSendEmail(Email.isSendEmail());
            goodRead = false;
        }
        try {
            myDriver.setUseAltDownloader(new Boolean(p.getProperty(p.useAltDownloader)).booleanValue());
        } catch (NullPointerException exc) {
            myDriver.setUseAltDownloader(Driver.DEFAULT_USE_ALT_DOWNLOADER);
            goodRead = false;
        }
        myGui.setLocation(x, y);
        myGui.setSize(width, height);
        return goodRead;
    }
