    @Test
    public void testHttpsConnection() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            String uri = "https://www.pivotaltracker.com";
            HttpGet get = new HttpGet(uri);
            HttpResponse execute = client.execute(get);
            byteArrayOutputStream = new ByteArrayOutputStream();
            execute.getEntity().writeTo(byteArrayOutputStream);
        } finally {
            byteArrayOutputStream.close();
        }
    }
