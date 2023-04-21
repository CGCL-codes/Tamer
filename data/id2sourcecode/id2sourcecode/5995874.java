    @Override
    public void setUp() throws Exception {
        final URL url = this.getClass().getResource("GameExample.xml");
        final InputStream input = url.openStream();
        gameData = (new GameParser()).parse(input, false);
    }
