    private void testWithContentFilter(boolean chunked) throws Exception {
        Server server = TestUtil.createServer(5555);
        server.start();
        InterceptorConfigurationBuilder configBuilder = new InterceptorConfigurationBuilder();
        configBuilder.setTargetHost("localhost");
        configBuilder.setTargetPort(5555);
        configBuilder.setListenPort(8000);
        configBuilder.setRequestContentFilterFactory(new ContentFilterFactory() {

            public StreamFilter[] getContentFilterChain(MimeType contentType) {
                try {
                    return new StreamFilter[] { new ReplaceFilter("pattern", "replacement", "ascii") };
                } catch (UnsupportedEncodingException ex) {
                    return null;
                }
            }
        });
        InterceptorConfiguration config = configBuilder.build();
        Interceptor interceptor = new Interceptor(config, new Dump(System.out));
        HttpClient client = TestUtil.createClient(config);
        HttpPost request = new HttpPost(TestUtil.getBaseUri(config, server) + "/echo");
        request.setEntity(TestUtil.createStringEntity("test-pattern-test", "utf-8", chunked));
        HttpResponse response = client.execute(request);
        assertEquals("test-replacement-test", TestUtil.getResponseAsString(response));
        interceptor.halt();
        server.stop();
    }
