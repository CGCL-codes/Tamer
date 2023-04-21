    @Override
    public void setUp() throws Exception {
        final URL url = SerializationTest.class.getResource("Test.xml");
        final InputStream input = url.openStream();
        (new GameParser()).parse(input, false);
    }
