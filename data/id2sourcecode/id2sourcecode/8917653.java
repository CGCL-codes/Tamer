    public static byte[] readBinary(URL url) {
        InputStream in = null;
        try {
            in = url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copyAll(in, out);
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(in);
        }
    }
