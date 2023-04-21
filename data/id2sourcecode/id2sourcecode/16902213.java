    public int getEncodeValue(URL url) {
        InputStream stream;
        try {
            stream = url.openStream();
        } catch (IOException e) {
            stream = null;
        }
        return getEncodeValue(stream);
    }
