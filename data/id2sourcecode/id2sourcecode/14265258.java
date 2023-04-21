    private XdmNode loadHelp(URL url) throws IOException, SaxonApiException {
        DocumentBuilder builder = Shell.getProcessor().newDocumentBuilder();
        InputStream is = url.openStream();
        try {
            Source source = new StreamSource(is);
            XdmNode root = builder.build(source);
            return root;
        } finally {
            is.close();
        }
    }
