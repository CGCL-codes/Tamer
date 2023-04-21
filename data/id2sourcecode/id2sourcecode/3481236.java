    public static void main(String[] args) throws Exception {
        System.out.println("Before load : " + Util.getUsedMemory());
        File file = new File(LARGE_FILE_PATH);
        int size = (int) file.length();
        Util.pause();
        long beforeTime1 = System.currentTimeMillis();
        byte[] buf = new byte[size];
        FileInputStream fis = new FileInputStream(file);
        fis.read(buf);
        System.out.println("Before after array load : " + Util.getUsedMemory());
        Util.pause();
        FileChannel ch = fis.getChannel();
        MappedByteBuffer mbuf = ch.map(MapMode.READ_ONLY, 0, size);
        System.out.println("Before memory mapped I/O : " + Util.getUsedMemory());
        Util.pause();
        fis.close();
    }
