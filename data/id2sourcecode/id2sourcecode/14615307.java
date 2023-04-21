    public void load(final URL url) {
        new Streams.using<BufferedInputStream, Exception>() {

            @Override
            public BufferedInputStream open() throws Exception {
                return new BufferedInputStream(url.openStream());
            }

            @Override
            public void handle(BufferedInputStream stream) throws Exception {
                load(stream);
            }

            @Override
            public void happen(Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }
