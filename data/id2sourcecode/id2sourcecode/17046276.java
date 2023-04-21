    public static byte[] decompress(byte[] data, int pos, int length) {
        byte[] compressedData = new byte[length];
        System.arraycopy(data, pos + 50, compressedData, 0, length);
        InputStream compressedInputStream = new ByteArrayInputStream(compressedData);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(compressedInputStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c;
        try {
            while ((c = inflaterInputStream.read()) != -1) out.write(c);
        } catch (IOException e) {
            throw new RecordFormatException(e.toString());
        }
        return out.toByteArray();
    }
