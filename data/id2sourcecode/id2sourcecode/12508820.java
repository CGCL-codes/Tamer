    public static RunAutomaton load(URL url) throws IOException, OptionalDataException, ClassCastException, ClassNotFoundException, InvalidClassException {
        return load(url.openStream());
    }
