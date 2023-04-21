    public AppletMachineFactory(ZmppApplet applet, URL storyurl, URL resourceurl, boolean savetofile) throws Exception {
        this.applet = applet;
        savegamestore = savetofile ? new FileSaveGameDataStore(applet) : new MemorySaveGameDataStore();
        try {
            storyis = storyurl.openStream();
            if (resourceurl != null) {
                resourceis = resourceurl.openStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
