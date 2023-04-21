    protected void setUp() throws Exception {
        super.setUp();
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final URL url = getClass().getResource("XStreamer.xsl");
        transformer = transformerFactory.newTransformer(new StreamSource(url.openStream()));
    }
