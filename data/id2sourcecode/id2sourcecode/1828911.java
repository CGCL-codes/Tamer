    private void makeRequestInNewContext(String url) throws IOException {
        GoogleHttpClient client = new GoogleHttpClient(getContext(), "Test", false);
        try {
            HttpGet method = new HttpGet(url);
            HttpResponse response = client.execute(method);
        } finally {
            client.close();
        }
    }
