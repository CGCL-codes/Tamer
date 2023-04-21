    public static void pipe(InputStream in, OutputStream out) {
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
