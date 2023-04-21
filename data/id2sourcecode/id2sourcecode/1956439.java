    public void read(final URL url) throws IOException, DataFormatException {
        final URLConnection connection = url.openConnection();
        final int fileSize = connection.getContentLength();
        if (fileSize < 0) {
            throw new FileNotFoundException(url.getFile());
        }
        final InputStream stream = url.openStream();
        try {
            read(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
