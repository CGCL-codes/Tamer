    public static byte[] readRemoteBytes(String httpURL) throws IOException, MalformedURLException {
        URL url = new URL(httpURL);
        URLConnection urlConnection = url.openConnection();
        int contentLength = urlConnection.getContentLength();
        if (contentLength == -1) {
            throw new IOException("Invalid bytes size");
        }
        InputStream inputStream = urlConnection.getInputStream();
        return readBytes(inputStream, contentLength);
    }
