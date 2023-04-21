    public void verifyAndProcess(URL url) throws ProcessorException {
        try {
            url.openConnection();
        } catch (IOException e) {
            throw new UserError("Could not open a connection to \"" + url.toExternalForm() + "\".", e);
        }
        process(url);
    }
