    @Override
    public void setUp() throws Exception {
        final URL url = this.getClass().getResource("Test.xml");
        final InputStream input = url.openStream();
        m_data = (new GameParser()).parse(input, false);
    }
