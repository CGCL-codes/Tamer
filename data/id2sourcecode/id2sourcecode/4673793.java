    private long transferFromImpl(Reader in, long maxCount) throws IOException {
        long count = 0;
        if (maxCount < 0) {
            for (long step; (step = in.skip(Long.MAX_VALUE)) > 0; ) count += step;
        } else {
            for (long step; (count < maxCount) && ((step = in.skip(maxCount - count)) > 0); ) count += step;
        }
        return count;
    }
