    public DOMWrapper parse(URL url) throws XOMException {
        try {
            return parse(new BufferedInputStream(url.openStream()));
        } catch (IOException ex) {
            throw new XOMException(ex, "Document parse failed");
        }
    }
