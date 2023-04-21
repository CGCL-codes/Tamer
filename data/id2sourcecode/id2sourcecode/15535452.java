    public void read(final URL url) throws IOException, DataFormatException {
        final URLConnection connection = url.openConnection();
        final int contentLength = connection.getContentLength();
        if (contentLength < 0) {
            throw new FileNotFoundException(url.getFile());
        }
        final InputStream stream = connection.getInputStream();
        try {
            read(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
