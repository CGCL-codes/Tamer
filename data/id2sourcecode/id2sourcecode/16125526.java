    public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int len;
        try {
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        } finally {
            in.close();
        }
    }
