    public static void readFileAndWriteToWriter(String path, Writer writer) throws IOException {
        readAndWriteToWriter(new FileReader(path), writer);
    }
