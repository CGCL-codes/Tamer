    private static byte[] getBytes(InputStream inStream) throws IOException {
        if (inStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(10 * 1024);
        byte[] b = new byte[1024];
        while (true) {
            int read = inStream.read(b);
            if (read == 0 || read == -1) {
                break;
            }
            outStream.write(b, 0, read);
        }
        return outStream.toByteArray();
    }
