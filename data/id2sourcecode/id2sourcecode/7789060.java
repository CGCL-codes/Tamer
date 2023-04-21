    private static void copy(final Reader reader, final Writer writer) throws IOException {
        final Reader in = new BufferedReader(reader, 1024 * 16);
        int i;
        while ((i = in.read()) != -1) {
            writer.write(i);
        }
    }
