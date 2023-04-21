    public synchronized void connect() throws IOException {
        connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDefaultUseCaches(false);
    }
