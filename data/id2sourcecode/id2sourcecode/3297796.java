    public void testClasspathUrlHandlerGoodUrl() throws Exception {
        URL url = null;
        char[] buf = new char[18];
        url = new URL("classpath://com.ail.coretest/TestUrlContent.xml");
        InputStream in = url.openStream();
        InputStreamReader isr = new InputStreamReader(in);
        isr.read(buf, 0, 18);
        assertEquals("<root>hello</root>", new String(buf));
    }
