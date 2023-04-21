    public void start() throws IndexException {
        try {
            raf = new RandomAccessFile(file, "rws");
            channel = raf.getChannel();
            buffer = ByteBuffer.allocate(RECORD_SIZE);
            channel.position(0);
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            positionCount = buffer.getInt();
            recordCount = buffer.getInt();
        } catch (IOException e) {
            throw new IndexException(e);
        }
    }
