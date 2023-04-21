    @Test
    public void testApacheHttpClient4ExecutorSharedHttpClientFinalize() throws Throwable {
        HttpClient httpClient = new DefaultHttpClient();
        ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(httpClient);
        ClientRequest request = new ClientRequest(generateURL("/test"), executor);
        ClientResponse<?> response = request.post();
        Assert.assertEquals(204, response.getStatus());
        executor.finalize();
        Assert.assertEquals(httpClient, executor.getHttpClient());
        HttpPost post = new HttpPost(generateURL("/test"));
        HttpResponse httpResponse = httpClient.execute(post);
        Assert.assertEquals(204, httpResponse.getStatusLine().getStatusCode());
        httpClient.getConnectionManager().shutdown();
    }
