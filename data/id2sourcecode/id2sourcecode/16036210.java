    @Override
    public long transferFrom(Readable in, long maxCount) throws IOException {
        if (this.xout != null) return this.xout.transferFrom(in, maxCount); else return super.transferFrom(in, maxCount);
    }
