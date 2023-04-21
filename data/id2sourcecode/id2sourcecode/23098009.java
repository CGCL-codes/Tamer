    private byte[] getBytesFromFileNIO(File file) throws IOException {
        log.debug("Reading file NIO::NAME::" + file.getName(), "getBytesFromFileNIO");
        FileInputStream is = new FileInputStream(file);
        FileChannel fc = is.getChannel();
        long length = fc.size();
        log.debug("Reading file NIO::SIZE::" + length, "getBytesFromFileNIO");
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large");
        }
        byte[] bytes = new byte[(int) length];
        ByteBuffer buffer = ByteBuffer.allocate((int) length);
        fc.read(buffer);
        log.debug("Reading file into BUFFER::", "getBytesFromFileNIO");
        bytes = buffer.array();
        fc.close();
        log.debug("Closing File channel and returning::", "getBytesFromFileNIO");
        return bytes;
    }
