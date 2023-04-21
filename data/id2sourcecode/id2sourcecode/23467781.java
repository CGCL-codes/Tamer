    public static byte[] unGzip(byte[] original) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(original.length << 2);
        GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(original), original.length);
        byte[] buffer = new byte[original.length];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }
