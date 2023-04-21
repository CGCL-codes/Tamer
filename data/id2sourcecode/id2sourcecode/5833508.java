    public static Sequence getSequence(URL url) throws InvalidMfiDataException, IOException {
        return getSequence(new BufferedInputStream(url.openStream()));
    }
