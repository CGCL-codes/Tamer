    protected static HttpResponse doPost(HttpPost post, HttpClient client, HttpContext context) throws Exception {
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = client.execute(post, context);
        return resp;
    }
