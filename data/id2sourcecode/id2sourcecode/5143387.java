    public static boolean testConnection() {
        boolean retval = false;
        String connURL = InterfaceFactory.getGlobalSettings().getProperties().getProperty(GlobalSettings.EJBCAWS_URL);
        if (connURL == null || connURL.trim().equals("")) {
            connURL = InterfaceFactory.getGlobalSettings().getProperties().getProperty(GlobalSettings.WSRA_URL).trim();
        } else {
            connURL = connURL.trim();
        }
        try {
            URL url = new URL(connURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            if (br.readLine() != null) {
                retval = true;
            }
        } catch (MalformedURLException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error couldn't connect to WebService interface : " + e.getMessage(), e);
        } catch (IOException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error couldn't connect to WebService interface : " + e.getMessage(), e);
        }
        return retval;
    }
