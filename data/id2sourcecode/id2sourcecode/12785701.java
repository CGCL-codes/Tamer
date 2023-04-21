    public InputStreamMetaDataCursor(final URL url, String[] columnDelimiters, String[] rowDelimiters, String nullRepresentation, Function<Object, ? extends Tuple> tupleFactory) throws IOException {
        this(new AbstractFunction<Object, InputStream>() {

            @Override
            public InputStream invoke() {
                try {
                    return url.openStream();
                } catch (IOException ioe) {
                    throw new WrappingRuntimeException(ioe);
                }
            }
        }, columnDelimiters, rowDelimiters, nullRepresentation, tupleFactory);
    }
