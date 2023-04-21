    public HttpRequest(URL url, String method) throws RequestException {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
        } catch (IOException e) {
            throw new RequestException(e);
        }
    }
