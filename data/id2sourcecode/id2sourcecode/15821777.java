    @Override
    protected ByteBuffer fetchData(URL url, long lastModified) throws IOException {
        return Charset.forName("UTF-16BE").encode(readAll(openConnection(url)));
    }
