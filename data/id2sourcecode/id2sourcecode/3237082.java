    public static String fileRead(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        bb = ByteBuffer.allocate((int) fc.size());
        fc.read(bb);
        fc.close();
        fis.close();
        String content = new String(bb.array(), "UTF-8");
        bb = null;
        return content;
    }
