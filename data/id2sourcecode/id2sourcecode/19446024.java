    protected String executeGet(String url) throws AuthenticationException, IOException {
        HttpResponse response = executeGetWithResponse(url);
        assertEquals(SC_OK, statusCode(response));
        return EntityUtils.toString(response.getEntity());
    }
