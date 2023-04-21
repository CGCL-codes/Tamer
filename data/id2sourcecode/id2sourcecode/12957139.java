    private void pump(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count;
        while ((count = in.read(buffer, 0, buffer.length)) >= 0) out.write(buffer, 0, count);
        out.flush();
    }
