    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection connection = super.openConnection(url);
        RemoteSession session = clientConnection.getSession();
        if (session != null && session.getSessionId() != null) {
            connection.setRequestProperty("Cookie", SESSION_COOKIE_NAME + "=" + session.getSessionId());
        }
        return connection;
    }
