    @Override
    public InputStream read() throws IOException {
        inputStream = url.openStream();
        return new BufferedInputStream(inputStream);
    }
