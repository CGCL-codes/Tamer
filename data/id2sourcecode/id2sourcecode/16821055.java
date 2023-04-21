    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) >= 0) out.write(buf, 0, len);
        } finally {
            in.close();
            out.close();
        }
    }
