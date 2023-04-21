    public static void saveTo(String path, InputStream in) throws IOException {
        if (in == null) throw new IllegalArgumentException("input stream cannot be null");
        if (path == null) throw new IllegalArgumentException("path cannot be null");
        byte[] bytes = new byte[128];
        FileOutputStream fout = new FileOutputStream(path);
        for (int x = in.read(bytes); x != -1; x = in.read(bytes)) fout.write(bytes, 0, x);
        fout.flush();
        fout.close();
    }
