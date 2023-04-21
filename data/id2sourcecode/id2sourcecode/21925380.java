    @Converter
    public static InputStream toInputStream(URL url) throws IOException {
        return url.openStream();
    }
