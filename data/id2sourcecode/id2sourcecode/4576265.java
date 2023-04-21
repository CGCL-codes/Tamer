    public static DataRaster read(File file) throws IOException {
        DataRaster raster = null;
        RandomAccessFile sourceFile = null;
        try {
            sourceFile = open(file);
            FileChannel channel = sourceFile.getChannel();
            AVList metadata = new AVListImpl();
            readUHL(channel, DTED_UHL_OFFSET, metadata);
            readDSI(channel, DTED_DSI_OFFSET, metadata);
            readACC(channel, DTED_ACC_OFFSET, metadata);
            raster = readElevations(channel, DTED_DATA_OFFSET, metadata);
        } finally {
            close(sourceFile);
        }
        return raster;
    }
