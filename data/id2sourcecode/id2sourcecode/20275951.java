    private byte[] transformToByteStream(String contractPath) {
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(contractPath);
            FileChannel fc = fis.getChannel();
            data = new byte[(int) fc.size()];
            ByteBuffer bb = ByteBuffer.wrap(data);
            fc.read(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
