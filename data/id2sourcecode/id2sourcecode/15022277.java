    private byte[] loadData(String name) throws IOException {
        System.out.println("reading:" + CorelatorTest.class.getResource(name).getFile());
        FileInputStream fis = new FileInputStream(CorelatorTest.class.getResource(name).getFile());
        FileChannel fch = fis.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(5000);
        int nbytes = fch.read(buff);
        System.out.println("read " + nbytes + " bytes");
        byte[] b = new byte[nbytes];
        buff.clear();
        buff.get(b);
        fch.close();
        fis.close();
        buff = null;
        return b;
    }
