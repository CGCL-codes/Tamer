    public VDEXVocabulary(URL url, String type) {
        super(url, type);
        try {
            _initWithDocument(_documentFromInputStream(url.openStream()));
        } catch (IOException ioe) {
            IllegalArgumentException iae = new IllegalArgumentException("An exception occurred reading the url " + url);
            iae.initCause(ioe);
            throw iae;
        }
    }
