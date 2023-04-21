    public AbstractPVRFileSystem(final File image) throws IOException {
        fileName = image.getAbsolutePath();
        offset = 0;
        maximumClusters = image.length() / CLUSTER_SIZE;
        if (image.length() == 0) {
            maximumClusters = Long.MAX_VALUE;
        }
        imageInputStream = new FileInputStream(image);
        imageFileChannel = imageInputStream.getChannel();
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        reverseBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }
