    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL(this.url.toString());
        if (!url.getProtocol().startsWith("http")) {
            throw new IOException("Unsupported scheme:" + url.getProtocol());
        }
        return ((HttpURLConnection) url.openConnection());
    }
