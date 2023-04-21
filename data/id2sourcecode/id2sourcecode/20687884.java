    @Test
    public void load() throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        final URL url = cl.getResource("named.default.yml");
        new Streams.using<InputStream, Exception>() {

            @Override
            public InputStream open() throws Exception {
                return url.openStream();
            }

            @Override
            public void handle(InputStream stream) throws Exception {
                event(stream);
            }

            @Override
            public void happen(Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }
