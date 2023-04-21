    protected Clob newClobFromResource(final String resource) throws Exception {
        Clob clob = createClob();
        InputStream inputStream = null;
        Reader reader = null;
        Writer writer = null;
        try {
            final URL url = getResource(resource);
            inputStream = url.openStream();
            reader = new InputStreamReader(inputStream);
            writer = clob.setCharacterStream(1);
            InOutUtil.copy(reader, writer);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            } else {
            }
        }
        return clob;
    }
