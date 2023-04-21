    public SimplePropertiesIconRepository() {
        properties = new Properties();
        try {
            URL url = SimplePropertiesIconRepository.class.getResource(PROPERTIES_FILE_NAME);
            properties.load(url.openStream());
        } catch (Exception e) {
            throw new Error("Icons file not found: " + PROPERTIES_FILE_NAME);
        }
    }
