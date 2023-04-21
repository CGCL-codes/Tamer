    public static void copyFile(InputStream src, OutputStream dest) throws IOException {
        BufferedInputStream in = new BufferedInputStream(src);
        BufferedOutputStream out = new BufferedOutputStream(dest);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        out.flush();
    }
