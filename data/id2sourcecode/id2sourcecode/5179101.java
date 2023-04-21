    public Sequence getSequence(URL url) throws InvalidMidiDataException, IOException {
        InputStream is = url.openStream();
        try {
            return getSequence(is);
        } finally {
            is.close();
        }
    }
