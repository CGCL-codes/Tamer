    public void decodeFromUrl(final URL url) throws DataFormatException, IOException {
        final URLConnection connection = url.openConnection();
        if (connection.getContentLength() < 0) {
            throw new FileNotFoundException(url.getFile());
        }
        decodeFromStream(connection.getInputStream());
    }
