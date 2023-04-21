    public void print(File file) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int i;
        while ((i = in.read(buf)) >= 0) out.write(buf, 0, i);
        in.close();
    }
