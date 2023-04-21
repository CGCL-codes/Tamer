    public static void transfer(FileChannel fileChannel, ByteChannel byteChannel, long lengthInBytes, long chunckSizeInBytes, boolean verbose, boolean fromFile) throws IOException {
        long overallBytesTransfered = 0L;
        long time = -System.currentTimeMillis();
        while (overallBytesTransfered < lengthInBytes) {
            long bytesTransfered = 0L;
            if (fromFile) {
                bytesTransfered = fileChannel.transferTo(0, Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered), byteChannel);
            } else {
                bytesTransfered = fileChannel.transferFrom(byteChannel, overallBytesTransfered, Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered));
            }
            overallBytesTransfered += bytesTransfered;
            if (verbose) {
            }
        }
        time += System.currentTimeMillis();
        if (verbose) {
        }
    }
