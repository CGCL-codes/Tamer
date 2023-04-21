    private void copyInputStream(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        out.flush();
        out.close();
        in.close();
    }
