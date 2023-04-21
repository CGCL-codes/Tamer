    public static void readAndWriteToWriter(Reader reader, Writer writer) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        char[] buffer = new char[2048];
        int chars = 0;
        while ((chars = in.read(buffer)) >= 0) {
            writer.write(buffer, 0, chars);
        }
        in.close();
    }
