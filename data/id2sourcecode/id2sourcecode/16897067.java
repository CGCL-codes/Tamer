    public void write(OutputStream out) throws IOException {
        InputStream in = getInputStream();
        byte buffer[] = new byte[4096];
        int n_read;
        while ((n_read = in.read(buffer)) != -1) {
            out.write(buffer, 0, n_read);
        }
    }
