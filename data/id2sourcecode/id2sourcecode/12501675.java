    public Sequence getSequence(URL url) throws InvalidMidiDataException, IOException {
        InputStream is = new BufferedInputStream(url.openStream());
        return getSequence(is);
    }
