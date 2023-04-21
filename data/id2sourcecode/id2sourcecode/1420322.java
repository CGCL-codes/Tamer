    public Properties setNewPrjProperties() {
        Properties propNewPrj = new Properties();
        try {
            propNewPrj.load(urlNewPrj.openStream());
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
        return propNewPrj;
    }
