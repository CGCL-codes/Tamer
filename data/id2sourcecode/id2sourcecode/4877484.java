    public void saveVXMLSettings(String path) {
        ProgressLogger.getInstance().debug("enter saveVXMLSettings(String)");
        Properties prop = new Properties();
        prop.setProperty("voice", Integer.toString(processThread.getCurrVoice()));
        prop.setProperty("volume", Float.toString(processThread.getTTS_volume()));
        prop.setProperty("pitch", Float.toString(processThread.getTTS_pitch()));
        prop.setProperty("rate", Float.toString(processThread.getTTS_rate()));
        prop.setProperty("engine", Integer.toString(processThread.getCurrEngine()));
        prop.setProperty("echoMode", InputWindow.getEchoMode() ? Integer.toString(1) : Integer.toString(0));
        prop.setProperty("write_to_disk", Boolean.toString(processThread.isReadFromDisk()));
        try {
            prop.storeToXML(new FileOutputStream(path), "VXMLSurfer settings");
        } catch (Exception e) {
            ProgressLogger.getInstance().error("Could not save VXML settings", e);
            e.printStackTrace();
        }
        ProgressLogger.getInstance().debug("exit saveVXMLSettings(String)");
    }
