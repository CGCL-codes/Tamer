    public HttpURLConnection connect(String targetUrl, String method) throws HttpRequestException {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            return connection;
        } catch (MalformedURLException e) {
            throw new HttpRequestException(e);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
