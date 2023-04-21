    public void testRandom() throws Exception {
        final HttpHost target = getServerHttp();
        int[] sizes = new int[] { 10, 2048, 4100, 0, -1 };
        for (int i = 0; i < sizes.length; i++) {
            String uri = "/random/" + sizes[i];
            if (sizes[i] < 0) uri += "/";
            HttpGet request = new HttpGet(uri);
            HttpClientConnection conn = connectTo(target);
            httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
            httpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
            httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, request);
            request.setParams(new DefaultedHttpParams(request.getParams(), defaultParams));
            httpExecutor.preProcess(request, httpProcessor, httpContext);
            HttpResponse response = httpExecutor.execute(request, conn, httpContext);
            response.setParams(new DefaultedHttpParams(response.getParams(), defaultParams));
            httpExecutor.postProcess(response, httpProcessor, httpContext);
            assertEquals("(" + sizes[i] + ") wrong status in response", HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            byte[] data = EntityUtils.toByteArray(response.getEntity());
            if (sizes[i] >= 0) assertEquals("(" + sizes[i] + ") wrong length of response", sizes[i], data.length);
            conn.close();
        }
    }
