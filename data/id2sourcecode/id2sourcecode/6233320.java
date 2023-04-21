    public void run() {
        ZapListings3 zap = new ZapListings3();
        GetListings gl = new GetListings();
        if (m_dlg != null) m_dlg.start("Starting Update...");
        Vector channels;
        try {
            channels = zap.getChannelList(m_config.getPostalcode(), m_config.getZipcode(), m_config.getProvider(), m_dlg, m_config.getDebug());
        } catch (Exception e) {
            m_dlg.end("My Telly not configured correctly", "Please configure My Telly through the Tools/Configure menu item: " + e.getMessage(), true);
            return;
        }
        PrintStream debugOut = null;
        if (m_config.getDebug()) {
            try {
                debugOut = new PrintStream(new FileOutputStream(new File("debug_out.txt")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                debugOut = null;
            }
        } else debugOut = null;
        m_dlg.update("Writing channels.xml file...", null);
        gl.writeChannelsToFile(channels, debugOut);
        m_dlg.update("channels.xml written.", null);
        try {
            gl.grab(channels, m_config.getPostalcode(), m_config.getZipcode(), m_config.getProvider(), m_startDate, m_config.getDaysToDownload(), m_config.getTimeout() * 1000, m_dlg, m_dlg, debugOut);
        } catch (Exception e) {
            m_dlg.end("My Telly not configured correctly", "Please configure My Telly through the Tools/Configure menu item: " + e.getMessage(), true);
            return;
        }
        m_dlg.update("Importing XML...", "Importing XML...");
        SQLProgramData pData = (SQLProgramData) Programs.getInstance().getProgramData();
        SQLChannelData channelData = (SQLChannelData) Channels.getInstance().getChannelData();
        try {
            pData.resetPrograms();
            channelData.resetChannels();
            XMLTVtoSQL xmltv = new XMLTVtoSQL(pData, channelData);
            xmltv.beginImport("channels.xml");
            int impCount = xmltv.beginImport("programs.xml");
        } catch (Exception e) {
            e.printStackTrace();
            m_dlg.end("Error", "Unable to import XML: " + e.getMessage(), true);
        }
        m_dlg.end("Done", "Finished importing XML.", false);
        if (debugOut != null) {
            debugOut.close();
        }
    }
