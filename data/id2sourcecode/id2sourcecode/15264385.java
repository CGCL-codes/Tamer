    protected static byte[] getUrlContentsAsBytes(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        InputStream urlInputStream = url.openStream();
        int urlFileLength = connection.getContentLength();
        byte[] urlFileData = new byte[urlFileLength];
        urlInputStream.read(urlFileData);
        return urlFileData;
    }
