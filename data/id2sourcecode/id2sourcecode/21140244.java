    private void passThroughContent(HttpEntity entity) throws IOException {
        int c = 0;
        InputStream content = entity.getContent();
        OutputStream responseBody = exchange.getResponseBody();
        while ((c = content.read()) != -1) responseBody.write(c);
        responseBody.close();
    }
