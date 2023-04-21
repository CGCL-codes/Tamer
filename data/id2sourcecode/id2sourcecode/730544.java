    private void loadProperties() {
        URL codeBase = getCodeBase();
        try {
            URL urlProperties = new URL(codeBase, "JmolApplet.properties");
            appletProperties = new PropertyResourceBundle(urlProperties.openStream());
        } catch (Exception ex) {
            System.out.println("JmolApplet.loadProperties():" + "JmolApplet.properties not found/loaded");
        }
    }
