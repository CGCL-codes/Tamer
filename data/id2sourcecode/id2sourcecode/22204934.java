    public ID3v11Tag(RandomAccessFile file, String loggingFilename) throws TagNotFoundException, IOException {
        setLoggingFilename(loggingFilename);
        FileChannel fc;
        ByteBuffer byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
        fc = file.getChannel();
        fc.position(file.length() - TAG_LENGTH);
        fc.read(byteBuffer);
        byteBuffer.flip();
        read(byteBuffer);
    }
