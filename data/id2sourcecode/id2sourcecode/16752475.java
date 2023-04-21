    public HttpResponse executeHttp(final HttpUriRequest request) throws ClientProtocolException, IOException, HttpException {
        return executeHttp(request, HttpStatus.SC_OK, HttpStatus.SC_MULTIPLE_CHOICES);
    }
