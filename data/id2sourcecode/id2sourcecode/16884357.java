    public static void bufferedStreamCopy_noCloseOut(InputStream in, OutputStream out) throws IOException {
        int bufSizeHint = 1;
        int read = -1;
        byte[] buf = new byte[bufSizeHint];
        BufferedInputStream from = new BufferedInputStream(in, bufSizeHint);
        BufferedOutputStream to = new BufferedOutputStream(out, bufSizeHint);
        while ((read = from.read(buf, 0, bufSizeHint)) >= 0) {
            to.write(buf, 0, read);
        }
    }
