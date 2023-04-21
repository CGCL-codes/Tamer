    public static void convertEncoding(File file, String readEncode, String writeEncode) {
        Reader reader = Streams.fileInr(file, readEncode);
        String s = Streams.readAndClose(reader);
        Writer writer = Streams.fileOutw(file, writeEncode);
        Streams.writeAndClose(writer, s);
    }
