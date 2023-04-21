    private byte[] _read(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        java.nio.channels.FileChannel fcl = fis.getChannel();
        byte[] byts = new byte[(int) fcl.size()];
        ByteBuffer bbr = ByteBuffer.wrap(byts);
        fcl.read(bbr);
        return byts;
    }
