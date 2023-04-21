    public static HttpResponse doPost(HttpPost post, HttpClient client) throws Exception {
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = client.execute(post);
        return resp;
    }
