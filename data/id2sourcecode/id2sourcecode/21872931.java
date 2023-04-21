    protected void loadProperties() {
        try {
            URL url = getPropertiesURL(propertiesPath);
            Properties p = new Properties();
            PropUtils.loadProperties(p, url.openStream());
            ClientRegistry.setOpenmapProperties(p);
        } catch (IOException ex) {
            throw new SeismoException(ex);
        }
    }
