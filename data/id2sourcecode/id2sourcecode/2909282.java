    private HttpURLConnection initConnection() throws MalformedURLException, IOException, ProtocolException {
        if (log.isDebugEnabled()) {
            log.debug("Initialize the HttpURLConnection Object");
        }
        URL url = new URL(endPoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setAllowUserInteraction(true);
        connection.setDefaultUseCaches(false);
        return connection;
    }
