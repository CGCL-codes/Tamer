    public void write(File file, long audioStartLocation) throws IOException {
        logger.info("Writing tag to file");
        byte[] bodyByteBuffer = writeFramesToBuffer().toByteArray();
        int sizeIncPadding = calculateTagSize(bodyByteBuffer.length + TAG_HEADER_LENGTH, (int) audioStartLocation);
        int padding = sizeIncPadding - (bodyByteBuffer.length + TAG_HEADER_LENGTH);
        ByteBuffer headerBuffer = writeHeaderToBuffer(padding, bodyByteBuffer.length);
        if (sizeIncPadding > audioStartLocation) {
            logger.finest("Adjusting Padding");
            adjustPadding(file, sizeIncPadding, audioStartLocation);
        }
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "rw").getChannel();
            fc.write(headerBuffer);
            fc.write(ByteBuffer.wrap(bodyByteBuffer));
            fc.write(ByteBuffer.wrap(new byte[padding]));
        } finally {
            if (fc != null) {
                fc.close();
            }
        }
    }
