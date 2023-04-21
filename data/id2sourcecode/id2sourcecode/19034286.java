    @Send
    public InputStream send(XRequest request, String requestString) throws IOException {
        StringEntity requestEntity = new StringEntity(requestString, "UTF-8");
        requestEntity.setContentType("text/xml");
        HttpPost post = new HttpPost(serviceEndPointUrl.get());
        post.setEntity(requestEntity);
        HttpResponse response = httpClient.execute(post);
        final HttpEntity responseEntity = response.getEntity();
        return new FilterInputStream(responseEntity.getContent()) {

            @Override
            public void close() throws IOException {
                responseEntity.consumeContent();
                super.close();
            }
        };
    }
