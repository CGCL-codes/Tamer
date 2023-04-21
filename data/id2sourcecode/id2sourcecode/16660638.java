    private void init(URL url, URL idx) throws IOException, ReadFailedException, URISyntaxException {
        setSourceKey(new SAMKey(url.toString()));
        content = new SeekableFileCachedHTTPStream(url);
        size = url.openConnection().getContentLength();
        File tmpBAI = File.createTempFile("urlbam", ".bai");
        tmpBAI.deleteOnExit();
        url = URIFactory.url(url.toString() + ".bai");
        copy(url.openStream(), tmpBAI);
        index = tmpBAI;
        deleteIndex = true;
    }
