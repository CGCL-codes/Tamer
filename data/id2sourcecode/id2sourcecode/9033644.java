    public void testUnconnectedStateParameters() throws Throwable {
        URL url = new URL("https://localhost:55555");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        try {
            connection.getCipherSuite();
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
        }
        try {
            connection.getPeerPrincipal();
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
        }
        try {
            connection.getLocalPrincipal();
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
        }
        try {
            connection.getServerCertificates();
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
        }
        try {
            connection.getLocalCertificates();
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
        }
    }
