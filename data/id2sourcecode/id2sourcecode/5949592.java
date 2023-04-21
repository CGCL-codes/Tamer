    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        debug.print("in=" + in + " out=" + out);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        in.close();
        out.close();
    }
