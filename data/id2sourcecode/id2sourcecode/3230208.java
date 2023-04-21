    private static ByteBuffer makeByteBuffer(File file) {
        try {
            RandomAccessFile randomIndex = new RandomAccessFile(file, "r");
            FileChannel fc = randomIndex.getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size());
            return byteBuffer;
        } catch (FileNotFoundException fnf) {
            throw new ResourceMissingException(file);
        } catch (IOException ioe) {
            throw new OperationFailedException("mapping byte buffer with index file", ioe);
        }
    }
