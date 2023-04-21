    public void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[copyBufferSize];
        int read = -1;
        while ((read = in.read(b, 0, b.length)) != -1) {
            out.write(b, 0, read);
        }
    }
