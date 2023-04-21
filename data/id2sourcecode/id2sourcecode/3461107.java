    public BSGraphicsProperties() {
        URL url = this.getClass().getResource("/bsgraphics.properties");
        if (url != null) {
            try {
                properties.load(url.openStream());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error reading existing bsgraphics.properties", e);
            }
        }
    }
