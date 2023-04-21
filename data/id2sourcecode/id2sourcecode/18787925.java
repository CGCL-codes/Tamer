    public static void transfer(Reader reader, Writer writer, int bufferSize) throws IOException {
        char transferBuf[] = new char[bufferSize];
        int readCount;
        while ((readCount = reader.read(transferBuf)) != -1) {
            writer.write(transferBuf, 0, readCount);
            writer.flush();
        }
    }
