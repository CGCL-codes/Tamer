    public synchronized DataImpl open(URL url) throws BadFormException, VisADException, IOException {
        return open(url.openStream());
    }
