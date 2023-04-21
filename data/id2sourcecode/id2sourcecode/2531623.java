    protected static byte[] getCurrentEntryContent(final JarInputStream jis) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int readLen = 0;
        final byte[] buffer = new byte[256000];
        while ((readLen = jis.read(buffer)) != -1) {
            baos.write(buffer, 0, readLen);
        }
        return baos.toByteArray();
    }
