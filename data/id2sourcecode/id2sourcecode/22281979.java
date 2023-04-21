    public void loadProperties() {
        try {
            InputStream pstream = null;
            if (new File(property_file_path).exists()) pstream = new FileInputStream(property_file_path); else {
                URL url = Thread.currentThread().getContextClassLoader().getResource("PeerserverProperties.properties");
                if (url != null) pstream = url.openStream();
            }
            properties.load(pstream);
        } catch (Exception e) {
        }
    }
