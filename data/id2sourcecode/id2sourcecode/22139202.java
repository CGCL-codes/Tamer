    public static InputSupplier<InputStream> newInputStreamSupplier(final URL url) {
        checkNotNull(url);
        return new InputSupplier<InputStream>() {

            public InputStream getInput() throws IOException {
                return url.openStream();
            }
        };
    }
