    @Deprecated
    public static byte[] readStreamAsBytes(final InputStream stream) throws IOException {
        BufferedInputStream buffered;
        if (stream instanceof BufferedInputStream) buffered = (BufferedInputStream) stream; else buffered = new BufferedInputStream(stream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int readVal = buffered.read();
        while (readVal != -1) {
            out.write(readVal);
            readVal = buffered.read();
        }
        stream.close();
        return out.toByteArray();
    }
