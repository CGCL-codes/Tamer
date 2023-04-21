    public byte[] decompress(byte[] in) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteArrayInputStream bais = new ByteArrayInputStream(in);
        final GZIPInputStream gz = new GZIPInputStream(bais);
        int read;
        while ((read = gz.read()) != -1) {
            baos.write(read);
        }
        gz.close();
        bais.close();
        baos.close();
        return baos.toByteArray();
    }
