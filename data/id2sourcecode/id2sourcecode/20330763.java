    private FileHeader readHeader(File file, FileConfig config) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(FileHeader.HEADER_LENGHT);
        FileChannel readChannel = null;
        try {
            readChannel = new RandomAccessFile(file, "r").getChannel();
            readChannel.read(buffer, 0);
        } finally {
            readChannel.close();
        }
        buffer.flip();
        long fileTail = buffer.getLong();
        int keyTreeMin = buffer.getInt();
        int valueRevTreeMin = buffer.getInt();
        byte valueCompressed = buffer.get();
        byte compressionCodec = buffer.get();
        int blockSize = buffer.getInt();
        FileHeader header = new FileHeader();
        header.setFileTail(fileTail);
        header.setKeyTreeMin(keyTreeMin);
        header.setValueRevTreeMin(valueRevTreeMin);
        header.setValueCompressed(valueCompressed);
        header.setCompressionCodec(compressionCodec);
        header.setBlockSize(blockSize);
        blockFileChannel = new BlockFileChannel(file, header.getBlockSize(), config.isBlockCache(), new CRC32ChecksumGenerator());
        dropTransfer = new DropTransfer(blockFileChannel);
        return header;
    }
