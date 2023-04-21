    public void insert(final URL url, final Container container) throws Exception {
        Reader reader = null;
        try {
            InputStream in = url.openStream();
            if (in == null) {
                throw new IOException(IO_ERROR_MSG + url.toString());
            }
            reader = new InputStreamReader(in);
            insert(reader, container);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
    }
