    public void test5xxStatusThrowsIOExceptionwhenTryingToRead() throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("test:status:500;file:test/soapdust/urlhandler/dust/hello.txt").openConnection();
        try {
            connection.getInputStream();
            fail();
        } catch (IOException e) {
        }
    }
