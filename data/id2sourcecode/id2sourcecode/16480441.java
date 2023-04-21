    public MappedRandomAccessFile(String filename, String mode) throws FileNotFoundException, IOException {
        if (mode.equals("rw")) init(new java.io.RandomAccessFile(filename, mode).getChannel(), FileChannel.MapMode.READ_WRITE); else init(new FileInputStream(filename).getChannel(), FileChannel.MapMode.READ_ONLY);
    }
