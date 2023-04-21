    private void pump(InputStream in, OutputStream out, int size) throws IOException {
        byte[] buffer = new byte[size];
        int count;
        while ((count = in.read(buffer, 0, size)) >= 0) out.write(buffer, 0, count);
        out.flush();
    }
