    public static void copy(Reader reader, Writer writer) {
        try {
            char[] buf = new char[1024];
            for (; ; ) {
                int n = reader.read(buf);
                if (n < 0) {
                    break;
                }
                writer.write(buf, 0, n);
            }
        } catch (IOException e) {
            throw new IOError(e.getMessage(), e);
        }
    }
