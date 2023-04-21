    @Override
    public InputStream getBundleFileAsStream() throws IOException {
        URL url = mBundle.getResource(mBundleFile);
        return url.openStream();
    }
