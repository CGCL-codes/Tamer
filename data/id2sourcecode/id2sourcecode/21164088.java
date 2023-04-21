    private void write(InputStream in, OutputStream out) throws IOException {
        in = new BufferedInputStream(in, 512);
        out = new BufferedOutputStream(out, 512);
        byte[] buffer = new byte[512];
        int c = 0;
        while ((c = in.read(buffer, 0, buffer.length)) != -1) out.write(buffer, 0, c);
        out.flush();
    }
