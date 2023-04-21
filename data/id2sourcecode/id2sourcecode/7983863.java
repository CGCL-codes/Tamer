    public HttpRequest(String url, String method) throws RequestException {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
        } catch (IOException e) {
            throw new RequestException(e);
        }
    }
