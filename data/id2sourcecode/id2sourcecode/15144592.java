    public static byte[] readToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
        byte[] bytes = new byte[4096];
        int readBytes;
        while ((readBytes = in.read(bytes)) > 0) outputStream.write(bytes, 0, readBytes);
        return outputStream.toByteArray();
    }
