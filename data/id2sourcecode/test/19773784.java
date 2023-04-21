    public static void main(String[] argv) throws IOException {
        RandomAccessFile rf = new RandomAccessFile(argv[0], "r");
        MappedByteBuffer bb = rf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, 0x150);
        init(bb);
        int i;
        do {
            i = readBits(8);
            System.out.println("8 bits = " + i + " == " + Integer.toBinaryString(i));
        } while (bb.hasRemaining() || bitsBuffered > 0);
    }
