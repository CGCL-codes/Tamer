    static ReadBuffer create(RandomAccessFile file) throws IOException {
        FileChannel ch = file.getChannel();
        long size = ch.size();
        if (canUseFileMap() && (size <= Integer.MAX_VALUE)) {
            MappedByteBuffer buf;
            try {
                buf = ch.map(FileChannel.MapMode.READ_ONLY, 0, size);
                ch.close();
                return new MappedReadBuffer(buf);
            } catch (IOException exp) {
                exp.printStackTrace();
                System.err.println("File mapping failed, will use direct read");
            }
        }
        return new FileReadBuffer(file);
    }
