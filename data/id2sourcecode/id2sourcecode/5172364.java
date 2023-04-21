    @Override
    public void setUp() throws Exception {
        final URL url = this.getClass().getResource("Test.xml");
        InputStream input = url.openStream();
        m_dataSource = (new GameParser()).parse(input, false);
        input = url.openStream();
        m_dataSink = (new GameParser()).parse(input, false);
    }
