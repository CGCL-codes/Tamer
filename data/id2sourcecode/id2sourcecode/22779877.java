    public static void copyText(final Reader reader, final Writer writer) throws IOException {
        assert null != reader;
        assert null != writer;
        final char[] buffer = new char[512];
        while (true) {
            final int n = reader.read(buffer);
            if (n > 0) {
                writer.write(buffer, 0, n);
            } else {
                break;
            }
        }
    }
