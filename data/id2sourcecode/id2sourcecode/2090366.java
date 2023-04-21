    public void waitForSolr(String context) throws Exception {
        int port = getLocalPort();
        URL url = new URL("http://localhost:" + port + context + "/select?q={!raw+f=junit_test_query}ping");
        Exception ex = null;
        for (int i = 0; i < 600; i++) {
            try {
                InputStream stream = url.openStream();
                stream.close();
            } catch (IOException e) {
                ex = e;
                Thread.sleep(200);
                continue;
            }
            return;
        }
        throw new RuntimeException("Jetty/Solr unresponsive", ex);
    }
