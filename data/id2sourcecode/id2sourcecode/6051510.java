    @Override
    public HttpEntity call() throws Exception {
        try {
            HttpResponse response = httpClient.execute(request, context);
            HttpEntity entity = response.getEntity();
            return entity;
        } catch (ClientProtocolException e) {
            request.abort();
            throw new DownloadException("protocol error", e);
        } catch (IOException e) {
            request.abort();
            throw new DownloadException("I/O error", e);
        }
    }
