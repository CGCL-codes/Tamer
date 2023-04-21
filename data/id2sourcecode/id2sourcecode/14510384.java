    @Override
    public void copyStream2Stream(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[BLOCK_SIZE];
        int read = 0;
        while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
    }
