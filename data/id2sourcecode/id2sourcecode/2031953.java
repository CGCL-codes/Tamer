    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] block = new byte[512];
        while (true) {
            int readLength = inputStream.read(block);
            if (readLength == -1) break;
            byteArrayOutputStream.write(block, 0, readLength);
        }
        byte[] retValue = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return retValue;
    }
