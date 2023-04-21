    public long transferFrom(InputStream in, long maxCount) throws IOException {
        return DataIOImpl.transferBytes(in, this, maxCount);
    }
