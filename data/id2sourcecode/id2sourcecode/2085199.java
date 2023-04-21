    public static byte[] unpackRaw(byte[] b) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        GZIPInputStream zis = new GZIPInputStream(bais);
        byte[] tmpBuffer = new byte[256];
        int n;
        while ((n = zis.read(tmpBuffer)) >= 0) baos.write(tmpBuffer, 0, n);
        zis.close();
        return baos.toByteArray();
    }
