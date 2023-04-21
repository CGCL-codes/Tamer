    DefaultDisplay(final Reader reader, final Writer writer) {
        assert reader != null;
        assert writer != null;
        m_reader = new SecureBufferedReader(reader);
        m_writer = new PrintWriter(writer, true) {

            @Override
            public void close() {
            }
        };
    }
