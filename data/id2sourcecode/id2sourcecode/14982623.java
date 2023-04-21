    public void copy(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws IOException {
        byte[] b = new byte[copyBufferSize];
        int read = -1;
        while ((read = in.read()) != -1) {
            out.write(b, 0, read);
        }
        if (closeIn) {
            in.close();
        }
        if (closeOut) {
            out.close();
        }
    }
