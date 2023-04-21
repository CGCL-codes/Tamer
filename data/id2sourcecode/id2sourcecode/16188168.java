    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new ArchiveURLConnection(url);
    }
