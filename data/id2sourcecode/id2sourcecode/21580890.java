    public void testX509TrustManager() {
        try {
            echo("Test to open a SLL connection with the X509 SSL trust manager...");
            File certFile = new File(getTestDataPath(), "cert" + File.separator + "www.amazon.de.crt");
            X509Certificate cert = OvX509Utils.loadX509Certificate(certFile);
            assertTrue(cert != null);
            OvX509Utils.installHttpsURLConnectionTrustManager(new OvX509TrustManager(Collections.singletonList(cert)));
            URL url = new URL("https://www.amazon.de");
            Object content = url.openConnection().getContent();
            assertTrue(content != null);
        } catch (Throwable t) {
            fail(t.getMessage());
            if (log.isErrorEnabled()) {
                log.error(t.getMessage(), t);
            }
        }
    }
