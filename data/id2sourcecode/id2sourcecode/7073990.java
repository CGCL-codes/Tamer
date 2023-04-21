    protected InputSource loadExternalWsdl(String aActualLocation) throws RuntimeException {
        logger.debug("loadExternalWsdl() " + aActualLocation);
        try {
            URL url = new URL(aActualLocation);
            return new InputSource(url.openStream());
        } catch (MalformedURLException e) {
            logger.error("Error: " + aActualLocation + " is not a valid url ");
            throw new RuntimeException(aActualLocation + " is not a valid url ", e);
        } catch (IOException e) {
            logger.error("Error: error loading wsdl from " + aActualLocation);
            throw new RuntimeException("error loading wsdl from " + aActualLocation, e);
        }
    }
