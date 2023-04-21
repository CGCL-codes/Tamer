    public void testEcho() throws Exception {
        final String message = "Hello, world!";
        final String charset = "UTF-8";
        final HttpHost target = getServerHttp();
        HttpPost request = new HttpPost("/echo/");
        request.setHeader("Host", target.getHostName());
        request.setEntity(new StringEntity(message, charset));
        HttpClientConnection conn = connectTo(target);
        httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        httpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
        httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, request);
        request.setParams(new DefaultedHttpParams(request.getParams(), defaultParams));
        httpExecutor.preProcess(request, httpProcessor, httpContext);
        HttpResponse response = httpExecutor.execute(request, conn, httpContext);
        response.setParams(new DefaultedHttpParams(response.getParams(), defaultParams));
        httpExecutor.postProcess(response, httpProcessor, httpContext);
        assertEquals("wrong status in response", HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        String received = EntityUtils.toString(response.getEntity());
        conn.close();
        assertEquals("wrong echo", message, received);
    }
