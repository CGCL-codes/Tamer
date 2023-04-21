    @Override
    public long transferFrom(InputStream in, long maxCount) throws IOException {
        if (this.xout == null) return super.transferFrom(in, maxCount); else return this.xout.transferFrom(in, maxCount);
    }
