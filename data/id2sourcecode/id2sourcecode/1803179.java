    public void readFileProperties(URL url) throws PropertiesManagerException {
        logger.info("readFileProperties(URL): opening connection...");
        URLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (URLConnection) url.openConnection();
            if (connection != null) {
                logger.info("readFileProperties(URL): opening connection... OK");
            } else {
                logger.info("readFileProperties(URL): connection null");
            }
            logger.info("readFileProperties(URL): getting inputStream...");
            inputStream = connection.getInputStream();
            logger.info("readFileProperties(URL): getting inputStream... OK");
            this.readFileProperties(inputStream);
        } catch (IOException e) {
            throw new PropertiesManagerException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }
