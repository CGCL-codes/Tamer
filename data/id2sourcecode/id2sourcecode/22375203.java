    public static byte[] decompressGZIP(byte[] raw) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
        try {
            GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(raw));
            byte[] buffer = new byte[4096];
            int read;
            do {
                read = gin.read(buffer);
                if (read > 0) {
                    bout.write(buffer, 0, read);
                }
            } while (read >= 0);
            return bout.toByteArray();
        } catch (IOException ex) {
            throw new AssertionError("IO Exception on a ByteArrayOutputStream?");
        }
    }
