    private static void printMessageSize(Message msg, String tagNamePrefix) {
        ByteBuffer buf = msg.encode();
        int len = buf.remaining();
        System.out.println(msg.getName() + ": " + len);
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(FILENAME_PREFIX + (tagNamePrefix != null ? tagNamePrefix : "") + msg.getName(), "rw");
        } catch (FileNotFoundException e) {
        }
        FileChannel ch = f.getChannel();
        try {
            ch.write(buf);
            ch.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
