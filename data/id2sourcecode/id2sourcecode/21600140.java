    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "createContentHandler", args = { java.lang.String.class }), @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies positive case, and java.lang.Error.", clazz = URLConnection.class, method = "setContentHandlerFactory", args = { ContentHandlerFactory.class }) })
    @SideEffect("This test affects tests that are run after this one." + " The reason are side effects due to caching in URLConnection." + " Maybe this test needs to be run in isolation.")
    public void test_createContentHandler() throws IOException {
        TestContentHandlerFactory factory = new TestContentHandlerFactory();
        if (isTestable) {
            assertFalse(isCreateContentHandlerCalled);
            URL url = new URL("http://" + Support_Configuration.SpecialInetTestAddress);
            URLConnection.setContentHandlerFactory(factory);
            URLConnection con = url.openConnection();
            try {
                con.getContent();
                assertTrue(isCreateContentHandlerCalled);
                assertTrue(isGetContentCalled);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            isGetContentCalled = false;
            try {
                con.getContent(new Class[] {});
                assertTrue(isGetContentCalled);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                con.setContentHandlerFactory(factory);
                fail("java.lang.Error was not thrown.");
            } catch (java.lang.Error e) {
            }
            try {
                con.setContentHandlerFactory(null);
                fail("java.lang.Error was not thrown.");
            } catch (java.lang.Error e) {
            }
        } else {
            ContentHandler ch = factory.createContentHandler("text/plain");
            URL url;
            try {
                url = new URL("http://" + Support_Configuration.SpecialInetTestAddress);
                assertNotNull(ch.getContent(url.openConnection()));
            } catch (MalformedURLException e) {
                fail("MalformedURLException was thrown: " + e.getMessage());
            } catch (IOException e) {
                fail("IOException was thrown.");
            }
        }
    }
