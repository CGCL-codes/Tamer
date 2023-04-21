        @Override
        public long transferFromByteChannel(final ReadableByteChannel in, final long maxCount) throws IOException {
            return transferFromByteChannel(in, maxCount, false);
        }
