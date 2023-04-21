    public static InputStream inputStream(String urlString) throws IOException {
        URLConnection uc = openConnection(urlString);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) uc;
        if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return uc.getInputStream();
        } else {
            throw new IOException("bad status code :" + httpUrlConnection.getResponseCode());
        }
    }
