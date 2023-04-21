    public static byte[] streamToBytes(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while (inStream.available() > 0) outStream.write(inStream.read());
        return outStream.toByteArray();
    }
