    protected void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
    }
