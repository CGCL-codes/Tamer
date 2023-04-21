    public HttpURLConnection getConnection(URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            if (pool_semaphore.tryAcquire(wait_timeout_millis, TimeUnit.MILLISECONDS)) {
                pool.add(connection);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }
