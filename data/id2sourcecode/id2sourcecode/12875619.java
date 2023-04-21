    public static final void copyInputStream(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
