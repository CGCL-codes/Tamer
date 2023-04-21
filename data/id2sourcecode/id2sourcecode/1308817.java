    public Graph readGraph(URL url) throws DataIOException {
        try {
            return readGraph(url.openStream());
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }
