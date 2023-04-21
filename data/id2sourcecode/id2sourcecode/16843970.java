    private byte[] _read(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        java.nio.channels.FileChannel fcl = fis.getChannel();
        byte[] byrsData = new byte[(int) fcl.size()];
        ByteBuffer bbr = ByteBuffer.wrap(byrsData);
        fcl.read(bbr);
        return byrsData;
    }
