    public PdfReader load(URL url) {
        try {
            return this.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
