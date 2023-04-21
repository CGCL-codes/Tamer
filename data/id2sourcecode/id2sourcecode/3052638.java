    public static void main(String[] args) {
        try {
            RandomAccessFile afile = new RandomAccessFile("\\temp\\1.txt", "rws");
            byte[] bytes = new byte[65536];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) i;
            }
            FileChannel fc = afile.getChannel();
            for (int i = 0; i < 2500; i++) {
                System.out.println(i * 65536 / (1024 * 1024));
                fc.write(ByteBuffer.wrap(bytes));
            }
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
