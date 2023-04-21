    private static void test4() throws Throwable {
        FileChannel rwChannel = new RandomAccessFile(new File("d:\\data.txt"), "rw").getChannel();
        int position = 4;
        ByteBuffer wrBuf = ByteBuffer.allocate((int) (rwChannel.size() - position));
        rwChannel.read(wrBuf, position);
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put("Heja".getBytes());
        buf.flip();
        wrBuf.flip();
        rwChannel.position(4);
        rwChannel.write(buf);
        rwChannel.position(position + 4);
        rwChannel.write(wrBuf);
        rwChannel.close();
    }
