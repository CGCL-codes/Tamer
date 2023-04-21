    public <T> T execute(final HttpHost target, final HttpRequest request, final ResponseHandler<? extends T> responseHandler, final HttpContext context) throws IOException, ClientProtocolException {
        if (responseHandler == null) {
            throw new IllegalArgumentException("Response handler must not be null.");
        }
        HttpResponse response = execute(target, request, context);
        T result;
        try {
            result = responseHandler.handleResponse(response);
        } catch (Throwable t) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Throwable t2) {
                    this.log.warn("Error consuming content after an exception.", t2);
                }
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            throw new UndeclaredThrowableException(t);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            entity.consumeContent();
        }
        return result;
    }
