    private void consume(Reader aReader, PrintWriter aWriter) throws IOException {
        char[] buff = new char[1024 * 4];
        int read;
        try {
            while ((read = aReader.read(buff)) != -1) {
                aWriter.write(buff, 0, read);
                if (aWriter.checkError()) {
                    break;
                }
            }
        } finally {
            SdlCloser.close(aReader);
        }
    }
