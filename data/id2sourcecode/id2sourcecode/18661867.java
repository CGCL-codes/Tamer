    public static boolean isEquals(File file, File file2) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file2, "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
        fc.read(buf);
        buf.flip();
        boolean isEquals = isEquals(file, new ByteBuffer[] { buf });
        fc.close();
        raf.close();
        return isEquals;
    }
