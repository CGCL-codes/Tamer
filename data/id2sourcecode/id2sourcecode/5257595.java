    public static byte[] slurpInputStream(InputStream is) throws IOException {
        final int chunkSize = 2048;
        byte[] buf = new byte[chunkSize];
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(chunkSize);
        int count;
        while ((count = is.read(buf)) != -1) byteStream.write(buf, 0, count);
        return byteStream.toByteArray();
    }
