    private static void copy(InputStream istr, OutputStream ostr) throws IOException {
        int len;
        byte[] buffer = new byte[1024 * 20];
        while ((len = istr.read(buffer)) > 0) ostr.write(buffer, 0, len);
        ostr.flush();
    }
