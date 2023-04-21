    public void init() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("JarHolder: attempting to connect to " + urlpath);
            }
            URL url = new URL(urlpath);
            conn = (JarURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();
            theJar = conn.getJarFile();
        } catch (IOException ioe) {
            log.error("JarHolder: error establishing connection to JAR at \"" + urlpath + "\"", ioe);
        }
    }
