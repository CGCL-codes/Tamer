    public static byte[] decompress(byte[] data, int compressionType) throws IOException {
        byte[] tmp = new byte[1024];
        int read;
        GZipInputStream zipInputStream = new GZipInputStream(new ByteArrayInputStream(data), 1024, compressionType, true);
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        while ((read = zipInputStream.read(tmp, 0, 1024)) > 0) {
            bout.write(tmp, 0, read);
        }
        return bout.toByteArray();
    }
