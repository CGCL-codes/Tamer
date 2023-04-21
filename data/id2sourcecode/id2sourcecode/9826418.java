    public void parse(URL url) throws IOException {
        LOG.info("parsing " + url);
        InputStream stream = url.openStream();
        try {
            parse(stream);
        } finally {
            stream.close();
        }
    }
