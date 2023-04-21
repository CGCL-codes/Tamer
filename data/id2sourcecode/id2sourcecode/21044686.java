    public static byte[] readProxy(String proxyPath) throws Exception {
        FileChannel fileCh = new FileInputStream(new File(proxyPath)).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(new Long(fileCh.size()).intValue());
        fileCh.read(byteBuffer);
        return byteBuffer.array();
    }
