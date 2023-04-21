    private static byte[] read(InputStream _in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[8 * 1024];
        for (; ; ) {
            int read = _in.read(buff);
            if (read == -1) {
                break;
            }
            out.write(buff, 0, read);
        }
        out.close();
        return out.toByteArray();
    }
