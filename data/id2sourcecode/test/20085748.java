    public InputStream getInputStream() throws IOException {
        URL url = null;
        try {
            url = new URL(urlName);
        } catch (MalformedURLException ex) {
            return null;
        }
        URLConnection urlConnection = url.openConnection();
        if (urlConnection instanceof HttpURLConnection) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        }
        return urlConnection.getInputStream();
    }
