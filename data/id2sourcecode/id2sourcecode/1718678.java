    public static void copyInputReader2OutWriter(Reader reader, Writer writer) throws IOException {
        int read = 0;
        char buffer[] = new char[8192];
        while ((read = reader.read(buffer)) > -1) {
            writer.write(buffer, 0, read);
        }
        reader.close();
        writer.close();
    }
