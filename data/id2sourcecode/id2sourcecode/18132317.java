    private final Response execute(HttpRequestBase request) throws ClientProtocolException, IOException {
        HttpResponse res = getHttpClient().execute(request, context);
        return new Response(res);
    }
