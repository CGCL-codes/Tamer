    private byte[] _readImageData(InputStream in) throws IOException, CacheException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = (in.read(buffer))) >= 0) out.write(buffer, 0, length);
        return out.toByteArray();
    }
