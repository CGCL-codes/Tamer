    public BufferedImage getBufferedImage(String sKey) throws IOException {
        URL url = getURL(sKey);
        if (url == null) {
            return null;
        }
        InputStream in = null;
        URLConnection urlConnection = url.openConnection();
        in = urlConnection.getInputStream();
        return readImage(readStream(in));
    }
