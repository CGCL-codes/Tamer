    private void testOldIO(RandomAccessFile in) throws Throwable {
        FileChannel channel = in.getChannel();
        byte[] buffer = new byte[5000];
        Date before = new Date();
        long len = in.length();
        for (int i = 0; i < 50; i++) {
            while (in.getFilePointer() < len) {
                in.read(buffer, 0, 1500);
            }
            in.seek(0);
        }
        Date after = new Date();
        double seconds = ((double) (after.getTime() - before.getTime())) / 1000;
        System.out.println("time(seconds)=" + seconds);
    }
