    private void executeDelete(String url) throws AuthenticationException, UnsupportedEncodingException, IOException, ClientProtocolException {
        HttpDelete request = new HttpDelete(SERVICE_BASE_URL + url);
        setCredentials(request);
        HttpResponse response = client.execute(request);
        consume(response.getEntity());
        assertEquals(SC_NO_CONTENT, statusCode(response));
    }
