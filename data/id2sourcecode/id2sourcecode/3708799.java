    @Test
    public void testGetPage() throws Exception {
        request.setUrl("http://www.google.pl");
        HttpResponse response = httpClient.execute(request);
        assertTrue(response.is2xxSuccess());
        assertTrue(response.getResponseHeaders().size() > 0);
        response.close();
    }
