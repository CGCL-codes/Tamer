    private void initializeJelly() {
        URL url = this.getClass().getClassLoader().getResource(schemaDir + schemaName);
        try {
            if ((url == null) || (url.openConnection() == null)) {
                logger.error("Unable to locate the schema file @ - " + (schemaDir + schemaName));
            } else {
                url.openConnection().connect();
            }
        } catch (IOException ioException) {
            logger.error("Unable to locate the schema file @ - " + url);
        }
        homePageHelper = new HomePageHelper(url, this);
    }
