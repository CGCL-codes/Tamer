    public void write(OutputStream fos) throws IOException {
        FileInputStream fis = new FileInputStream(this);
        byte[] buf = new byte[1024];
        int a = 0;
        while ((a = fis.read(buf)) != -1) fos.write(buf, 0, a);
        fis.close();
        fos.close();
        return;
    }
