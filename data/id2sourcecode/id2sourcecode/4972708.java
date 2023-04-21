    public static XdmNode asXdmNode(URL url) throws IOException, SaxonApiException {
        InputStream is = url.openStream();
        try {
            Source s = new StreamSource(is);
            s.setSystemId(url.toExternalForm());
            net.sf.saxon.s9api.DocumentBuilder builder = Shell.getProcessor().newDocumentBuilder();
            return builder.build(s);
        } finally {
            is.close();
        }
    }
