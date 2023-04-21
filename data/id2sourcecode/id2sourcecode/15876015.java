    public static Mp4BoxHeader seekWithinLevel(RandomAccessFile raf, String id) throws IOException {
        logger.finer("Started searching for:" + id + " in file at:" + raf.getChannel().position());
        Mp4BoxHeader boxHeader = new Mp4BoxHeader();
        ByteBuffer headerBuffer = ByteBuffer.allocate(HEADER_LENGTH);
        int bytesRead = raf.getChannel().read(headerBuffer);
        if (bytesRead != HEADER_LENGTH) {
            return null;
        }
        headerBuffer.rewind();
        boxHeader.update(headerBuffer);
        while (!boxHeader.getId().equals(id)) {
            logger.finer("Found:" + boxHeader.getId() + " Still searching for:" + id + " in file at:" + raf.getChannel().position());
            if (boxHeader.getLength() < Mp4BoxHeader.HEADER_LENGTH) {
                return null;
            }
            int noOfBytesSkipped = raf.skipBytes(boxHeader.getDataLength());
            logger.finer("Skipped:" + noOfBytesSkipped);
            if (noOfBytesSkipped < boxHeader.getDataLength()) {
                return null;
            }
            headerBuffer.rewind();
            bytesRead = raf.getChannel().read(headerBuffer);
            logger.finer("Header Bytes Read:" + bytesRead);
            headerBuffer.rewind();
            if (bytesRead == Mp4BoxHeader.HEADER_LENGTH) {
                boxHeader.update(headerBuffer);
            } else {
                return null;
            }
        }
        return boxHeader;
    }
