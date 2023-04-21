    public static DataSet loadDataSetNew(String filename) throws IOException, StreamException {
        FileInputStream in = new FileInputStream(filename);
        ReadableByteChannel channel = in.getChannel();
        DataSetStreamHandler handler = new DataSetStreamHandler(new HashMap(), new NullProgressMonitor());
        StreamTool.readStream(channel, handler);
        return handler.getDataSet();
    }
