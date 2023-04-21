    private InputStream getStreamFromUrl(URL url, String notFoundMessage) throws ApolloAdapterException {
        InputStream stream = null;
        if (url == null) {
            String message = "Couldn't find url for " + getInput();
            logger.error(message);
            throw new ApolloAdapterException(message);
        }
        if (url != null) {
            try {
                logger.info("Trying to open url " + url);
                stream = url.openStream();
                logger.debug("Succesfully opened url " + url);
            } catch (IOException e) {
                logger.error(notFoundMessage, e);
                stream = null;
                throw new ApolloAdapterException(notFoundMessage);
            }
        }
        return stream;
    }
