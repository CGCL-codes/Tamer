    public static byte[] decode(final byte[] compressed) throws IOException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(compressed);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        InflaterInputStream inflaterIn = null;
        try {
            inflaterIn = new InflaterInputStream(byteIn);
            int read;
            byte[] buffer = new byte[BUFFER_SIZE];
            do {
                read = inflaterIn.read(buffer);
                if (read > 0) {
                    byteOut.write(buffer, 0, read);
                }
            } while (read >= 0);
            return byteOut.toByteArray();
        } finally {
            inflaterIn.close();
            byteOut.close();
        }
    }
