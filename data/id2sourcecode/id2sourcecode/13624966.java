    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "createURLStreamHandler", args = { java.lang.String.class })
    @SideEffect("Leaves wrong StreamHandlerFactory behind, affects other tests")
    public void test_createURLStreamHandler() throws MalformedURLException {
        if (isTestable) {
            TestURLStreamHandlerFactory shf = new TestURLStreamHandlerFactory();
            assertFalse(isCreateURLStreamHandlerCalled);
            URL.setURLStreamHandlerFactory(shf);
            URL url = new URL("http://" + Support_Configuration.SpecialInetTestAddress);
            try {
                url.openConnection();
                assertTrue(isCreateURLStreamHandlerCalled);
                assertTrue(isOpenConnectionCalled);
            } catch (Exception e) {
                fail("Exception during test : " + e.getMessage());
            }
            try {
                URL.setURLStreamHandlerFactory(shf);
                fail("java.lang.Error was not thrown.");
            } catch (java.lang.Error e) {
            }
            try {
                URL.setURLStreamHandlerFactory(null);
                fail("java.lang.Error was not thrown.");
            } catch (java.lang.Error e) {
            }
        } else {
            TestURLStreamHandlerFactory shf = new TestURLStreamHandlerFactory();
            URLStreamHandler sh = shf.createURLStreamHandler("");
            assertNotNull(sh.toString());
        }
    }
