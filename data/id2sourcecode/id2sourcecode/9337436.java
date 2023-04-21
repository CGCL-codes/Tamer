    private ReceivedExceptionFactory() throws SRCPIOException {
        try {
            URL url = this.getClass().getResource(EXCEPTIONS_FILE);
            load(url.openStream());
        } catch (IOException x) {
            throw new SRCPIOException(x);
        }
    }
