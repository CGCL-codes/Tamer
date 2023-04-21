    public static int readRemoteBytesLength(String httpURL) throws IOException, MalformedURLException {
        URL url = new URL(httpURL);
        URLConnection urlConnection = url.openConnection();
        int contentLength = urlConnection.getContentLength();
        if (contentLength == -1) {
            throw new IOException("Invalid bytes size");
        }
        return contentLength;
    }
