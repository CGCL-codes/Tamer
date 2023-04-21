    public static PDFFile getPDFFile(final File file) throws FileNotFoundException, IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        return new PDFFile(buf);
    }
