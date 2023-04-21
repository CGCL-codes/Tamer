    public static void copy(final Reader reader, final Writer writer) throws IOException {
        Assert.notNull(reader, "reader");
        Assert.notNull(writer, "writer");
        int i = 0;
        char[] chars = new char[IOUtils.BUFFER_SIZE];
        do {
            i = reader.read(chars);
            if (i != -1) {
                writer.write(chars, 0, i);
            }
        } while (i != -1);
    }
