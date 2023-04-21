    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                InputStream resStream = this.getClass().getResourceAsStream("alloys.properties");
                if (resStream == null) {
                    URL url = new URL(ALLOYS_URL);
                    try {
                        resStream = url.openStream();
                        properties.load(resStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    properties.load(resStream);
                }
            } catch (IOException e) {
            }
        }
        return properties;
    }
