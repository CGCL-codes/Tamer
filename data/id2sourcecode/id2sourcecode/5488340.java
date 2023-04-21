    private boolean create(String path) throws IOException {
        File file = new File(path);
        if (file.exists() == false) {
            if (file.createNewFile() == false) {
                return false;
            }
            RandomAccessFile raFile = new RandomAccessFile(file, "rwd");
            FileChannel fc = raFile.getChannel();
            MappedByteBuffer mappedByteBuffer = fc.map(MapMode.READ_WRITE, 0, this.fileLimitLength);
            mappedByteBuffer.put(LogEntity.MAGIC.getBytes());
            mappedByteBuffer.putInt(1);
            mappedByteBuffer.putInt(-1);
            mappedByteBuffer.putInt(-2);
            mappedByteBuffer.force();
            MappedByteBufferUtil.clean(mappedByteBuffer);
            fc.close();
            raFile.close();
            return true;
        } else {
            return false;
        }
    }
