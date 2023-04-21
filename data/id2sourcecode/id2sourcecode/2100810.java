    @Test
    public void testPut() throws Exception {
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
        DefaultHttpClient client = new DefaultHttpClient(manager);
        HttpPut put = new HttpPut("http://localhost:8380/jseng/test3");
        put.setEntity(new StringEntity("mytest"));
        HttpResponse resp = client.execute(put);
        assertEquals(200, resp.getStatusLine().getStatusCode());
        assertEquals("just ok", resp.getStatusLine().getReasonPhrase());
        assertEquals("xxx", resp.getHeaders("X-Test")[0].getValue());
        assertEquals("text/plain", resp.getHeaders("Content-Type")[0].getValue());
        assertEquals("mytest", download(resp));
    }
