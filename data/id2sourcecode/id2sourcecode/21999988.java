    private HttpURLConnection getConnection(final URL url) throws IOException {
        if (this.proxy != null) {
            HttpURLConnection c = (HttpURLConnection) url.openConnection(this.proxy);
            return c;
        }
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        return c;
    }
