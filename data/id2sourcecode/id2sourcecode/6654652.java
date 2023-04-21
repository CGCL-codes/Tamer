    public void getPropertiesFromURL(Properties prop, URL url) {
        InputStream in;
        try {
            in = url.openStream();
        } catch (IOException e) {
            Logger.defaultLog("CfgRes", Logger.LOG_ERROR, "Could not open properties file '" + url + "'!");
            return;
        }
        try {
            prop.load(in);
        } catch (IOException e) {
            Logger.defaultLog("CfgRes", Logger.LOG_ERROR, "Could not read from properties file '" + url + "'!");
            return;
        }
        try {
            in.close();
        } catch (IOException e) {
            Logger.defaultLog("CfgRes", Logger.LOG_ERROR, "Could not close properties file '" + url + "'!");
            return;
        }
    }
