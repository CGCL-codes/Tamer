    public static void main(String[] args) throws Exception {
        File file = new File("c:/mem.txt");
        int length = (int) file.length();
        String mode = "rw";
        FileChannel fc = new RandomAccessFile(file, mode).getChannel();
        MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, length);
        String format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        while (true) {
            String now = sdf.format(new Date());
            byte[] b = now.getBytes();
            mbb.rewind();
            mbb.put(b, 0, b.length);
            mbb.force();
            Thread.sleep(1000);
        }
    }
