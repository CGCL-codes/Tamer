    public AppletMachineFactory(ZmppApplet applet, URL zblorburl, boolean savetofile) throws Exception {
        this.applet = applet;
        savegamestore = savetofile ? new FileSaveGameDataStore(applet) : new MemorySaveGameDataStore();
        try {
            resourceis = zblorburl.openStream();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
