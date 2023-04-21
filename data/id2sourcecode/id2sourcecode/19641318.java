    public void testDoGet() throws Exception {
        URL url = null;
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/dump/info?query=foo");
        assertTrue(IO.toString(url.openStream()).startsWith("<html>"));
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/");
        try {
            IO.toString(url.openStream());
            assertTrue(false);
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test");
        IO.toString(url.openStream());
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/");
        String s1 = IO.toString(url.openStream());
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/index.html");
        String s2 = IO.toString(url.openStream());
        assertEquals(s1, s2);
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/d.txt");
        assertTrue(IO.toString(url.openStream()).startsWith("0000"));
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/data.txt");
        String result = IO.toString(url.openStream());
        if (result.endsWith("\r\n")) {
            result = result.substring(0, result.length() - 2);
        } else if (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        } else {
            assertTrue(false);
        }
        assertTrue(result.endsWith("9999 3333333333333333333333333333333333333333333333333333333"));
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/dispatch/forward/dump/info?query=foo");
        assertTrue(IO.toString(url.openStream()).startsWith("<html>"));
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/dispatch/includeW/dump/info?query=foo");
        assertTrue(IO.toString(url.openStream()).startsWith("<H1>"));
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/dispatch/includeS/dump/info?query=foo");
        assertTrue(IO.toString(url.openStream()).startsWith("<H1>"));
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/dump/info?continue=1000");
        assertTrue(IO.toString(url.openStream()).startsWith("<html>"));
    }
