    public static void connect(InputStream in, OutputStream out, int bufsize) throws IOException {
        byte buf[] = new byte[bufsize];
        int read;
        while ((read = in.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        out.flush();
        in.close();
    }
