    private void stopToTracker() throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(config.get("stopURL") + "?id=" + config.get("id"));
            is = url.openStream();
            byte[] s = new byte[256];
            int i = 0;
            while ((i = is.read(s, 0, 256)) >= 0) {
            }
            is.close();
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "ERROR: check stopURL in config.txt");
        }
    }
