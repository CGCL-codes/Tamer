    @Deprecated
    @SuppressWarnings("unchecked")
    public HttpIO(URL url) throws IOException {
        log.setLevel(Level.INFO);
        connection = (T) url.openConnection();
        setupConnection();
        connected = false;
    }
