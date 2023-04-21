    public void test5xxStatusThrowsIOExceptionwhenTryingToRead() throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.Status500Servlet/").openConnection();
        try {
            connection.getInputStream();
            fail();
        } catch (IOException e) {
        }
    }
