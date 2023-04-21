    public void testReleaseConnection() throws Exception {
        HttpParams mgrpar = defaultParams.copy();
        ConnManagerParams.setMaxTotalConnections(mgrpar, 1);
        ThreadSafeClientConnManager mgr = createTSCCM(mgrpar, null);
        final HttpHost target = getServerHttp();
        final HttpRoute route = new HttpRoute(target, null, false);
        final int rsplen = 8;
        final String uri = "/random/" + rsplen;
        HttpRequest request = new BasicHttpRequest("GET", uri, HttpVersion.HTTP_1_1);
        ManagedClientConnection conn = getConnection(mgr, route);
        conn.open(route, httpContext, defaultParams);
        HttpResponse response = Helper.execute(request, conn, target, httpExecutor, httpProcessor, defaultParams, httpContext);
        assertEquals("wrong status in first response", HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        byte[] data = EntityUtils.toByteArray(response.getEntity());
        assertEquals("wrong length of first response entity", rsplen, data.length);
        try {
            getConnection(mgr, route, 10L, TimeUnit.MILLISECONDS);
            fail("ConnectionPoolTimeoutException should have been thrown");
        } catch (ConnectionPoolTimeoutException e) {
        }
        mgr.releaseConnection(conn, -1, null);
        conn = getConnection(mgr, route);
        assertFalse("connection should have been closed", conn.isOpen());
        conn.open(route, httpContext, defaultParams);
        httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        response = httpExecutor.execute(request, conn, httpContext);
        httpExecutor.postProcess(response, httpProcessor, httpContext);
        assertEquals("wrong status in second response", HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        data = EntityUtils.toByteArray(response.getEntity());
        assertEquals("wrong length of second response entity", rsplen, data.length);
        conn.markReusable();
        mgr.releaseConnection(conn, -1, null);
        conn = getConnection(mgr, route);
        assertTrue("connection should have been open", conn.isOpen());
        httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        response = httpExecutor.execute(request, conn, httpContext);
        httpExecutor.postProcess(response, httpProcessor, httpContext);
        assertEquals("wrong status in third response", HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        data = EntityUtils.toByteArray(response.getEntity());
        assertEquals("wrong length of third response entity", rsplen, data.length);
        mgr.releaseConnection(conn, -1, null);
        mgr.shutdown();
    }
