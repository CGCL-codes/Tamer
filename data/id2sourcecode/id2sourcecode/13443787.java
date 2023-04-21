    public IppRequest(URI uri, String user, String password) {
        request_id = incrementRequestIdCounter();
        requestUri = uri;
        try {
            URL url = new URL("http", user == null ? uri.getHost() : user + ":" + password + "@" + uri.getHost(), uri.getPort(), uri.getPath());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "application/ipp");
            connection.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
        } catch (IOException e) {
            logger.log(Component.IPP, "Unexpected IOException", e);
        }
        logger.log(Component.IPP, "[IppConnection] Host: " + uri.getHost() + " Port: " + uri.getPort() + " Path: " + uri.getPath());
    }
