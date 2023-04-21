    private InputStream getDAS2XMLStream() throws ApolloAdapterException {
        DASDsn theDSN = getDSN();
        logger.debug("DAS2Adapter.getDAS2XMLStream: theDSN = " + theDSN);
        if (theDSN == null) {
            return das2XmlFileInputStream(getInput());
        }
        String query = makeFeatureQuery();
        logger.debug("DAS2Adapter.getDAS2XMLStream: feature query = " + query);
        try {
            URL url = new URL(query);
            InputStream stream = url.openStream();
            Thread.sleep(50);
            if (stream.available() <= 1) {
                logger.error("Couldn't fetch requested region: " + query);
                return null;
            }
            return stream;
        } catch (Exception e) {
            logger.error("Couldn't open stream to " + query);
            throw new ApolloAdapterException("Couldn't open stream to " + query);
        }
    }
