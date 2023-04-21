    public static void copy(InputStream src, File dest) throws IOException {
        dest.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = src.read(b)) > 0) fos.write(b, 0, readBytes);
        fos.close();
    }
