    public void run() {
        URL url;
        try {
            url = new URL(getUrl());
            HttpURLConnection connection;
            if (proxy == null) {
                connection = (HttpURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection(proxy);
            }
            Response resp = null;
            try {
                initConnection(connection, this.getHTTPMethod(), this.getRequestData().length());
                cookieManager.setCookies(connection);
                connection.connect();
                IOUtils.write(connection.getOutputStream(), this.getRequestData());
                resp = readResponse(connection);
                cookieManager.storeCookies(connection);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            getCallback().onResponseReceived(null, resp);
        } catch (MalformedURLException e) {
            getCallback().onError(null, e);
        } catch (IOException e) {
            getCallback().onError(null, e);
        } catch (RequestException e) {
            getCallback().onError(null, e);
        }
    }
