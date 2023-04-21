    private URLConnection openConnection(final String methodName) throws RemoteException {
        boolean ok = false;
        URLConnection connection = null;
        try {
            connection = this.url.openConnection();
            connection.setAllowUserInteraction(false);
            connection.setDoOutput(true);
            connection.setReadTimeout(this.readTimeoutMillis);
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setUseCaches(false);
            if (connection instanceof HttpURLConnection) {
                final HttpURLConnection http = (HttpURLConnection) connection;
                http.setRequestMethod("POST");
            }
            connection.connect();
            ok = true;
            return connection;
        } catch (java.net.UnknownHostException ex) {
            throw new java.rmi.UnknownHostException(createErrorMsg("Unknown host", methodName), ex);
        } catch (final IOException ex) {
            throw new ConnectIOException(createErrorMsg("Can not connect to remote", methodName), ex);
        } finally {
            if (!ok) tryCloseConnection(connection);
        }
    }
