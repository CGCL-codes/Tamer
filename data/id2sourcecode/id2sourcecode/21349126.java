    public JSRandomAccessFile(String name, String mode, int block_count) throws FileNotFoundException, IOException {
        _block_count = block_count;
        _data_stream = new RandomAccessFile(name, mode);
        JSUtilFile.setODirect(_data_stream.getFD(), name, 1);
        _data_channel = _data_stream.getChannel();
    }
