    private void copy(InputStream from, OutputStream to) throws IOException {
        int n;
        while ((n = from.read(copyBuf)) != -1) to.write(copyBuf, 0, n);
    }
