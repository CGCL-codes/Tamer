    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[4096];
        int read;
        while ((read = in.read(buf)) > -1) {
            out.write(buf, 0, read);
        }
        out.flush();
    }
