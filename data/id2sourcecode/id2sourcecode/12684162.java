    private MappedByteBuffer makeReadOnlyMappedFile(RandomAccessFile file) {
        FileChannel fileChannel = file.getChannel();
        try {
            return fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
        } catch (IOException e) {
            throw new RuntimeException("cannot create mapped buffer", e);
        }
    }
