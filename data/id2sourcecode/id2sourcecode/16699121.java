    public static void makeNioKiloFile(long bytes, File file) {
        Random ranGen = new Random();
        ByteBuffer BB = ByteBuffer.allocate(1024);
        try {
            FileChannel chann = new FileOutputStream(file).getChannel();
            byte[] byteArr = new byte[1024];
            for (int i = 0; i < bytes; i++) {
                ranGen.nextBytes(byteArr);
                BB.put(byteArr);
                BB.flip();
                chann.write(BB);
                BB.clear();
            }
            chann.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
