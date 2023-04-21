    static void copy(InputStream in, OutputStream out) throws Exception {
        byte[] buf = new byte[1024];
        for (int len; (len = in.read(buf)) >= 0; ) out.write(buf, 0, len);
        out.flush();
        out.close();
        in.close();
    }
