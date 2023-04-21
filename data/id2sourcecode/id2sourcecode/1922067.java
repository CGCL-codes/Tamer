    public LocalProperties() {
        URL url = ClassLoader.getSystemResource("jllama.properties");
        try {
            this.load(url.openStream());
        } catch (IOException ex) {
            logger.error("Failed to load properties file");
        }
    }
