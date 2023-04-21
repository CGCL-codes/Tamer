    public void store(InputStream source) throws IOException {
        FileOutputStream fos = new FileOutputStream(this);
        byte[] buf = new byte[1024];
        int a = 0;
        while ((a = source.read(buf)) != -1) fos.write(buf, 0, a);
        source.close();
        fos.close();
        return;
    }
