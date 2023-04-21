    protected void build(OutputStream os) throws IOException {
        DataInputStream dis = new DataInputStream(classLoader.getResourceAsStream(resourceName));
        byte[] buf = new byte[256];
        int sz;
        while ((sz = dis.read(buf)) > 0) os.write(buf, 0, sz);
        dis.close();
    }
