    protected static URLConnection getURLConnection(URL url) {
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(true);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return connection;
        } catch (IOException e) {
            throw new SeismoException(e);
        }
    }
