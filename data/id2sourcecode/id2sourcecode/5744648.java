    public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
