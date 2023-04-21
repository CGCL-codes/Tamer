    private byte[] getNewData(String urlString) throws MalformedURLException, IOException {
        byte[] data;
        URL url = new URL(urlString);
        lastUrlString = urlString;
        InputStream in = url.openStream();
        data = NInputStream.readBytes(in);
        lastData = data;
        return data;
    }
