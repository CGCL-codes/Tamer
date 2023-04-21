    private HttpURLConnection openConnection() throws IOException {
        URL url = new URL(ONLINEJUDGE_URL + start);
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.connect();
        return urlConnection;
    }
