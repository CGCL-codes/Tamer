    private static byte[] readStream(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int count;
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        while ((count = in.read(buf)) != -1) out.write(buf, 0, count);
        return out.toByteArray();
    }
