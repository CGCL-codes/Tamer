    public static void pipeStreams(OutputStream to, InputStream from) throws IOException {
        BufferedInputStream in = new BufferedInputStream(from);
        BufferedOutputStream out = new BufferedOutputStream(to);
        byte[] buffer = new byte[8192];
        int read;
        while ((read = in.read(buffer, 0, 8192)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }
