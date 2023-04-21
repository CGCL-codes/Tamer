    private static void _io(final Reader reader, final Writer writer) throws IOException {
        char[] buffer = new char[512];
        try {
            while (true) {
                int n = reader.read(buffer);
                if (n == -1) {
                    break;
                }
                writer.write(buffer, 0, n);
            }
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }
