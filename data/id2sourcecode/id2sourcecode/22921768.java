    public static void copy(Reader reader, Writer writer, boolean close) throws IOException {
        int b = -1;
        do {
            b = reader.read();
            if (b != -1) {
                writer.write(b);
            }
        } while (b != -1);
        if (close) {
            reader.close();
            writer.flush();
            writer.close();
        }
    }
