    public Container render(final URL url) throws Exception {
        Reader reader = null;
        Container obj = null;
        try {
            InputStream in = url.openStream();
            if (in == null) {
                throw new IOException(IO_ERROR_MSG + url.toString());
            }
            reader = new InputStreamReader(in);
            obj = render(reader);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
        return obj;
    }
