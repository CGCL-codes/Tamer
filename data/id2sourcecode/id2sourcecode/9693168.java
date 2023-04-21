    private byte[] uncomressData(byte[] compressedData) throws IOException {
        int read;
        byte[] uncompressedData = new byte[10 * 1025];
        GZIPInputStream gzipInputStream;
        ByteArrayInputStream arrayInputStream;
        ByteArrayOutputStream arrayOutputStream;
        arrayInputStream = new ByteArrayInputStream(compressedData);
        arrayOutputStream = new ByteArrayOutputStream();
        gzipInputStream = new GZIPInputStream(arrayInputStream);
        while ((read = gzipInputStream.read(uncompressedData)) > 0) {
            arrayOutputStream.write(uncompressedData, 0, read);
        }
        uncompressedData = arrayOutputStream.toByteArray();
        return uncompressedData;
    }
