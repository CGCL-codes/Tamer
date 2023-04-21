    public static void transfer(Reader reader, OutputStream os, int bufferSize) throws IOException {
        char transferBuf[] = new char[bufferSize];
        int readCount;
        while ((readCount = reader.read(transferBuf)) != -1) {
            String lString = String.valueOf(transferBuf);
            os.write(lString.getBytes(), 0, readCount);
            os.flush();
        }
    }
