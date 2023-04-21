    public URLConnection makeN3URLConnection(URL url) throws IOException {
        final URLConnection openConnection = url.openConnection();
        openConnection.setRequestProperty("accept", "text/n3" + ", application/n3" + ", text/rdf+n3" + ", text/turtle" + ", application/rdf+xml, text/xml" + ", text/plain" + ", text/xhtml" + ", text/html");
        openConnection.setConnectTimeout(2000);
        return openConnection;
    }
