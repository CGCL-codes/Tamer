    private void writeMetadataSameSize(ByteBuffer rawIlstData, long oldIlstSize, long startIstWithinFile, FileChannel fileReadChannel, FileChannel fileWriteChannel) throws CannotWriteException, IOException {
        fileReadChannel.position(0);
        fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
        fileWriteChannel.position(startIstWithinFile);
        fileWriteChannel.write(rawIlstData);
        fileReadChannel.position(startIstWithinFile + oldIlstSize);
        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
    }
