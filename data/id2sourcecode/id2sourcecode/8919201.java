    public void dump(final OutputStream os) throws IOException {
        final byte[] outputBuffer = new byte[16 * 1024];
        int read = outputBuffer.length;
        while ((read = read(outputBuffer, 0, outputBuffer.length)) != -1) {
            os.write(outputBuffer, 0, read);
        }
        os.flush();
    }
