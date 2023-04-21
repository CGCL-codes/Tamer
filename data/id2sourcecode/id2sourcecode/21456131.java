    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getCipherSuite", args = {  })
    public final void test_getCipherSuite() {
        try {
            URL url = new URL("https://localhost:55555");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            try {
                connection.getCipherSuite();
                fail("IllegalStateException wasn't thrown");
            } catch (IllegalStateException ise) {
            }
        } catch (Exception e) {
            fail("Unexpected exception " + e + " for exception case");
        }
        try {
            HttpsURLConnection con = new MyHttpsURLConnection(new URL("https://www.fortify.net/"));
            assertEquals("CipherSuite", con.getCipherSuite());
        } catch (Exception e) {
            fail("Unexpected exception " + e);
        }
    }
