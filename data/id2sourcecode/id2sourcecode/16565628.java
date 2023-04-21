    public ReadableByteStream read(int i) throws FileOpFailure {
        WritableByteStream wbs = ByteStreamFactory.allocMemoryWritable(i);
        wbs.write(_carrier.read(i));
        return wbs.reader();
    }
