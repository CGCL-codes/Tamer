    public static byte[] decode(final byte[] compressed) throws IOException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(compressed);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        GZIPInputStream gzIn = null;
        try {
            gzIn = new GZIPInputStream(byteIn);
            int read;
            byte[] buffer = new byte[BUFFER_SIZE];
            do {
                read = gzIn.read(buffer);
                if (read > 0) {
                    byteOut.write(buffer, 0, read);
                }
            } while (read >= 0);
            return byteOut.toByteArray();
        } finally {
            gzIn.close();
            byteOut.close();
        }
    }
