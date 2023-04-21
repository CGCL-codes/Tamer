    public Sequence getSequence(URL url) throws InvalidMidiDataException, IOException {
        InputStream is = url.openStream();
        is = new BufferedInputStream(is, bisBufferSize);
        Sequence seq = null;
        try {
            seq = getSequence(is);
        } finally {
            is.close();
        }
        return seq;
    }
