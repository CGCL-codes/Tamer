    public static byte[] doDigest(MessageDigest digest, InputStream stream, long streamLength) throws IOException {
        stream = new BufferedInputStream(stream);
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = buffer.length;
        long byteCounter = 0;
        while (len != -1) {
            if (streamLength > -1 && byteCounter >= streamLength) break;
            len = stream.read(buffer, 0, len);
            byteCounter += len;
            if (streamLength > -1 && byteCounter >= streamLength) len = len - (int) (byteCounter - streamLength);
            if (len != -1) digest.update(buffer, 0, len);
        }
        return digest.digest();
    }
