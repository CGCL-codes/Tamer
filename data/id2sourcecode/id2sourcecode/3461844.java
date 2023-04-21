    public static byte[] obtainByteDataFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[512];
        int readBytes;
        while ((readBytes = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, readBytes);
        }
        byte[] byteData = outputStream.toByteArray();
        outputStream.close();
        return byteData;
    }
