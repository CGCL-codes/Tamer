    private HttpURLConnection createConnection(DataService dataService) throws IOException {
        URL url = dataService.getRemotePlace().getURL();
        if (!isHttpsSupported && url.getProtocol().equals("https")) {
            throw new IOException("HTTPS is currently disabled.");
        }
        return (HttpURLConnection) url.openConnection();
    }
