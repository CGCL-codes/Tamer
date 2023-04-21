    private byte[] _read(File fle) throws Exception {
        FileInputStream fis = new FileInputStream(fle);
        java.nio.channels.FileChannel fcl = fis.getChannel();
        byte[] byts = new byte[(int) fcl.size()];
        ByteBuffer bbr = ByteBuffer.wrap(byts);
        fcl.read(bbr);
        return byts;
    }
