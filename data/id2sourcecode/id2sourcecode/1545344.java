    public void read(final URL url) throws IOException, DataFormatException {
        final URLConnection connection = url.openConnection();
        final int length = connection.getContentLength();
        if (length < 0) {
            throw new FileNotFoundException(url.getFile());
        }
        read(url.openStream());
    }
