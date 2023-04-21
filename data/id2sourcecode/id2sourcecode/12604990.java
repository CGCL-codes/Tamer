    public Table readTable(URL url) throws DataIOException {
        try {
            return readTable(url.openStream());
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }
