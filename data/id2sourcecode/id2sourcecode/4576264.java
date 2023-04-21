    public static AVList readMetadata(File file) throws IOException {
        AVList metadata = null;
        RandomAccessFile sourceFile = null;
        try {
            sourceFile = open(file);
            FileChannel channel = sourceFile.getChannel();
            metadata = new AVListImpl();
            readUHL(channel, DTED_UHL_OFFSET, metadata);
            readDSI(channel, DTED_DSI_OFFSET, metadata);
            readACC(channel, DTED_ACC_OFFSET, metadata);
        } finally {
            close(sourceFile);
        }
        return metadata;
    }
