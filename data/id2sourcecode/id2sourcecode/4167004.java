    public void createRSSXML(String titleChannel) {
        FSAChannel ch = (FSAChannel) getChannels().get(titleChannel);
        try {
            ChannelExporterIF exporter = new RSS_2_0_Exporter(titleChannel + ".xml");
            exporter.write(ch);
        } catch (IOException ex) {
            Logger.getLogger(RSS.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
