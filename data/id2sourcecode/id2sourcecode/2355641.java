    public void addConfig(String configURL, boolean logIfMissing) {
        Log.debug(DEBUG_ADDING.getText() + " " + configURL);
        try {
            URL url = null;
            if (configURL.startsWith(RESOURCE_PREFIX)) {
                String resourceName = configURL.substring(RESOURCE_PREFIX.length());
                url = getClass().getClassLoader().getResource(resourceName);
                if (url == null) {
                    if (logIfMissing) Log.error(ERR_RESOURCE1.getText() + " [" + resourceName + "] " + ERR_RESOURCE2.getText());
                    return;
                }
            } else url = new URL(configURL);
            InputStream is = url.openStream();
            Properties configProperties = new Properties();
            configProperties.load(is);
            is.close();
            addConfig(configProperties);
        } catch (Exception e) {
            Log.error(ERR_ADDING.getText() + " [" + configURL + "] : ", e);
        }
    }
