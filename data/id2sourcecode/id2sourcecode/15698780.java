    public Sequence getSequence(URL url) throws InvalidMfiDataException, IOException {
        InputStream is = new BufferedInputStream(url.openStream());
        return getSequence(is);
    }
