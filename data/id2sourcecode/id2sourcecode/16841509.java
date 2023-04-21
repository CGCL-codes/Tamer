    @Override
    public long transferFrom(final InputStream in, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        if (maxCount < 0) maxCount = Long.MAX_VALUE;
        final Deflater deflater = deflater();
        final OutputStream out = out();
        deflater.next_in_stream = in;
        deflater.avail_in = maxCount;
        final long total_in = deflater.total_in;
        try {
            do {
                deflater.deflate(DeflaterFlushMode.Z_NO_FLUSH);
            } while ((deflater.avail_in > 0) || (deflater.avail_out == 0));
        } catch (final DeflaterException ex) {
            throw (ZipException) new ZipException(ex.getMessage()).initCause(ex);
        } finally {
            deflater.avail_in = 0;
            deflater.next_in_stream = null;
        }
        autoFlush(deflater, out);
        return deflater.total_in - total_in;
    }
