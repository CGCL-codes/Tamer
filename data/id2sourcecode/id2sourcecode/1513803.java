    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        try {
            return new IOFileURLConnection(url, IOFileUtils.createFile(url.toURI()));
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
