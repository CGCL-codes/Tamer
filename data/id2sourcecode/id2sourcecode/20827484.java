    public Sequence getSequence(URL url) throws InvalidSmafDataException, IOException {
        InputStream is = new BufferedInputStream(url.openStream());
        return getSequence(is);
    }
