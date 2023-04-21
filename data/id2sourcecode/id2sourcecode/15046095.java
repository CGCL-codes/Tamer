    public static byte[] readBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (true) {
            int read = is.read(buf);
            if (read == -1) {
                break;
            }
            baos.write(buf, 0, read);
            if (read < 1024) {
                break;
            }
        }
        return baos.toByteArray();
    }
