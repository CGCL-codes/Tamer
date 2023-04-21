    public static void createTestFiles() {
        try {
            System.out.println("Creating test files ... ");
            Random rand = new Random();
            String rootname = "f-";
            long[] sizes = { 0, 1, 50000000 };
            File testdir = new File(dirname);
            FileUtil.mkdirs(testdir);
            for (int i = 0; i < sizes.length; i++) {
                long size = sizes[i];
                File file = new File(testdir, rootname + String.valueOf(size));
                System.out.println(file.getName() + "...");
                FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
                long position = 0;
                while (position < size) {
                    long remaining = size - position;
                    if (remaining > 1024000) remaining = 1024000;
                    byte[] buffer = new byte[new Long(remaining).intValue()];
                    rand.nextBytes(buffer);
                    ByteBuffer bb = ByteBuffer.wrap(buffer);
                    position += fc.write(bb);
                }
                fc.close();
            }
            System.out.println("DONE\n");
        } catch (Exception e) {
            Debug.printStackTrace(e);
        }
    }
