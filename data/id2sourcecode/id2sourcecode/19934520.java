    @Override
    public final HttpResponse execute(HttpUriRequest request) throws IOException, ProtocolException {
        doExecute(request);
        return lastResponse;
    }
