    public static InputStream getStreamFromUrl(URL url, String badUrlMessage, String notFoundMessage) throws ApolloAdapterException {
        InputStream stream = null;
        try {
            logger.info("Trying to open URL " + url);
            stream = url.openStream();
            logger.debug("Succesfully opened URL " + url);
        } catch (IOException e) {
            ApolloAdapterException aae = new ApolloAdapterException(badUrlMessage);
            logger.error(badUrlMessage, aae);
            stream = null;
            throw aae;
        }
        try {
            int tries = 1500;
            do {
                Thread.sleep(10);
                tries--;
            } while (stream.available() <= 1 && tries > 0);
            if (stream.available() <= 1) {
                ApolloAdapterException aae = new ApolloAdapterException(notFoundMessage);
                logger.error(notFoundMessage, aae);
                stream = null;
                throw aae;
            }
        } catch (Exception e) {
            ApolloAdapterException aae = new ApolloAdapterException(notFoundMessage);
            logger.error(notFoundMessage, aae);
            stream = null;
            throw aae;
        }
        return stream;
    }
