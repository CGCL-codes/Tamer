    public void bind(final URL url) throws BindException {
        Reader reader = null;
        try {
            final InputStream in = url.openStream();
            if (in == null) {
                throw new BindException("Resource could not be found " + url.toString());
            }
            reader = new InputStreamReader(in);
            bind(reader);
        } catch (final IOException e) {
            System.err.println(e);
            throw new BindException(e);
        } finally {
            try {
                reader.close();
            } catch (final IOException ex) {
            }
        }
    }
