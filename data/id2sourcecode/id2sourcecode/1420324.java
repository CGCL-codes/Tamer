    public Properties setNewFileProperties() {
        Properties propNewFile = new Properties();
        try {
            propNewFile.load(urlNewFile.openStream());
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
        return propNewFile;
    }
