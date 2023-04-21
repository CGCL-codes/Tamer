    public void requestToVoid(final HttpUriRequest request) throws IOException {
        final HttpResponse response = httpClient.execute(request);
        response.getEntity().getContent().close();
    }
