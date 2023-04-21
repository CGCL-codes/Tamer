    public final HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null.");
        }
        return execute(determineTarget(request), request, context);
    }
