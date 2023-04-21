    public void write(File file, long audioStartLocation) throws IOException {
        logger.info(getLoggingFilename() + ":Writing tag to file");
        byte[] bodyByteBuffer = writeFramesToBuffer().toByteArray();
        logger.info(getLoggingFilename() + ":bodybytebuffer:sizebeforeunsynchronisation:" + bodyByteBuffer.length);
        if (TagOptionSingleton.getInstance().isUnsyncTags()) {
            unsynchronization = ID3Unsynchronization.requiresUnsynchronization(bodyByteBuffer);
        } else {
            unsynchronization = false;
        }
        if (isUnsynchronization()) {
            bodyByteBuffer = ID3Unsynchronization.unsynchronize(bodyByteBuffer);
            logger.info(getLoggingFilename() + ":bodybytebuffer:sizeafterunsynchronisation:" + bodyByteBuffer.length);
        }
        int sizeIncPadding = calculateTagSize(bodyByteBuffer.length + TAG_HEADER_LENGTH, (int) audioStartLocation);
        int padding = sizeIncPadding - (bodyByteBuffer.length + TAG_HEADER_LENGTH);
        logger.info(getLoggingFilename() + ":Current audiostart:" + audioStartLocation);
        logger.info(getLoggingFilename() + ":Size including padding:" + sizeIncPadding);
        logger.info(getLoggingFilename() + ":Padding:" + padding);
        ByteBuffer headerBuffer = writeHeaderToBuffer(padding, bodyByteBuffer.length);
        if (sizeIncPadding > audioStartLocation) {
            logger.info(getLoggingFilename() + ":Adjusting Padding");
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
