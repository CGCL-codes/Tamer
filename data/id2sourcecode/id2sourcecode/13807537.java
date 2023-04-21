    public static void copyAll(Reader reader, Writer writer) {
        try {
            char[] data = new char[4096];
            int count;
            while ((count = reader.read(data)) >= 0) writer.write(data, 0, count);
            reader.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
