    public Document load(URL url, DocumentBuilder builder) {
        Document doc = null;
        Exception parseException = null;
        try {
            LoadErrorHandler errorHandler = new LoadErrorHandler();
            builder.setErrorHandler(errorHandler);
            doc = builder.parse(url.openStream());
            parseException = errorHandler.getFirstException();
        } catch (Exception ex) {
            parseException = ex;
        }
        builder.setErrorHandler(null);
        if (parseException != null) {
            throw new RuntimeException("Unexpected exception " + parseException.getMessage(), parseException);
        }
        return doc;
    }
