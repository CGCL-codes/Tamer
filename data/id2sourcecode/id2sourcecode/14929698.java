    public InputStream getResource(URL url) throws MalformedURLException, IOException {
        InputStream is = url.openStream();
        if (is == null) throw new MalformedURLException("A problem occured while loading the resource (url.openStream() returned null)");
        return is;
    }
