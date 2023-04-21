    public void testAddCommandProcessor() throws Exception {
        String host = "localhost";
        int port = 8080;
        URLConnection connection = null;
        URL url = new URL("http://" + host + ":" + port + "/nonexistant");
        try {
            connection = url.openConnection();
            assertEquals(404, ((HttpURLConnection) connection).getResponseCode());
        } catch (IOException e) {
        }
        server.invoke(name, "addCommandProcessor", new Object[] { "nonexistant", new DummyCommandProcessor() }, new String[] { "java.lang.String", "mx4j.adaptor.http.HttpCommandProcessor" });
        connection = url.openConnection();
        assertEquals(200, ((HttpURLConnection) connection).getResponseCode());
        server.invoke(name, "removeCommandProcessor", new Object[] { "nonexistant" }, new String[] { "java.lang.String" });
        connection = url.openConnection();
        assertEquals(404, ((HttpURLConnection) connection).getResponseCode());
        server.invoke(name, "addCommandProcessor", new Object[] { "nonexistant", "test.mx4j.adaptor.http.HttpAdaptorTest$DummyCommandProcessor" }, new String[] { "java.lang.String", "java.lang.String" });
        connection = url.openConnection();
        assertEquals(200, ((HttpURLConnection) connection).getResponseCode());
    }
