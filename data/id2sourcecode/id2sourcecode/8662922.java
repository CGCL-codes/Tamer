    @Override
    public long transferToByteChannel(final WritableByteChannel dest, final long maxCount) throws IOException {
        if (maxCount == 0) {
            return 0;
        } else {
            ReadableByteChannel in = this.in;
            if (in == null) throw new IOException("closed"); else if (in instanceof FileChannel) return FileChannels.transferToByteChannel((FileChannel) in, dest, maxCount); else if (dest instanceof FileChannel) return FileChannels.transferFromByteChannel(in, (FileChannel) dest, maxCount); else return super.transferToByteChannel(dest, maxCount);
        }
    }
