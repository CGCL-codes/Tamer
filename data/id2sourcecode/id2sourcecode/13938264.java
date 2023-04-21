    public void load() throws SpatialIndexException {
        try {
            if (!rtreeFile.exists()) {
                return;
            }
            RandomAccessFile file = new RandomAccessFile(rtreeFile, "r");
            FileChannel channel = file.getChannel();
            MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buf.order(ByteOrder.LITTLE_ENDIAN);
            int numShapes = buf.getInt();
            for (int i = 0; i < numShapes; i++) {
                float xmin, ymin, xmax, ymax;
                int shapeIndex;
                xmin = buf.getFloat();
                ymin = buf.getFloat();
                xmax = buf.getFloat();
                ymax = buf.getFloat();
                shapeIndex = buf.getInt();
                Rectangle jsiRect = new Rectangle(xmin, ymin, xmax, ymax);
                rtree.add(jsiRect, shapeIndex);
            }
        } catch (Exception e) {
            throw new SpatialIndexException(e);
        }
    }
