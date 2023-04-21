    public void delete(RandomAccessFile file) throws IOException {
        logger.info("deleting tag from file if exists");
        FileChannel fc;
        ByteBuffer byteBuffer;
        fc = file.getChannel();
        fc.position(file.length() - (long) TAG_LENGTH);
        byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
        fc.read(byteBuffer);
        byteBuffer.rewind();
        if (seek(byteBuffer)) {
            logger.info("deleting v1 tag ");
            file.setLength(file.length() - (long) TAG_LENGTH);
        } else {
            logger.info("unable to find v1 tag to delete");
        }
    }
