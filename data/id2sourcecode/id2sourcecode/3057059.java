    @Test
    public void testClasspathUrlHandlerBadUrl() throws Exception {
        URL url = null;
        url = new URL("classpath://com.ail.core/TestUrlContentThatDoesNotExist.xml");
        try {
            url.openStream();
            fail("Open a resource that doesn't exist!");
        } catch (FileNotFoundException e) {
        }
    }
