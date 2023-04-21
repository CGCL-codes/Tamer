    public RESTCall(String method, String path, String user, String password) throws AgentException {
        URL url;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            throw new AgentException("Malformed URL: " + path, e);
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new AgentException("Failed to open URL connection to " + path, e);
        }
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new AgentException("Failed to use " + method + " method", e);
        }
        addAuth(user, password);
    }
