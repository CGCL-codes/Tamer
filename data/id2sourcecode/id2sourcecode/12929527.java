    protected ByteBuffer mapFile(File file, int length) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, filemode);
        length = prepFile(raf, length);
        FileChannel channel = raf.getChannel();
        ByteBuffer ret = channel.map(mapmode, 4, length);
        channel.close();
        raf.close();
        return ret;
    }
