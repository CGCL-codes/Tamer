    public static long copy(Reader reader, Writer writer) {
        assert reader != null;
        assert writer != null;
        char[] buffer = new char[1024 * 4];
        long count = 0;
        int n = 0;
        try {
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
                count += n;
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
