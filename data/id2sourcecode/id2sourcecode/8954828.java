    private void testNewIONonDirectBuffer(RandomAccessFile in) throws Throwable {
        FileChannel channel = in.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(5000);
        Date before = new Date();
        long len = channel.size();
        for (int i = 0; i < 50; i++) {
            System.out.println("i=" + i);
            while (channel.position() < len) {
                System.out.print("position=" + channel.position());
                channel.read(buffer, 1500);
                System.out.print("  pos2=" + channel.position());
                byte[] temp = buffer.array();
                System.out.println("  size=" + temp.length);
            }
            channel.position(0);
        }
        Date after = new Date();
        double seconds = ((double) (after.getTime() - before.getTime())) / 1000;
        System.out.println("time(seconds)=" + seconds);
    }
