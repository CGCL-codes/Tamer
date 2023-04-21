    private static void copy(InputStream inStream, OutputStream outStream) throws IOException {
        int b;
        while ((b = inStream.read()) != -1) outStream.write(b);
    }
