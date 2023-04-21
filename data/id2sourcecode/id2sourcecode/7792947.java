        @Override
        public final long transferFromByteChannel(final ReadableByteChannel in, final long maxCount) throws IOException {
            final SinkChannel sink = this.sink;
            if (sink == null) throw new ClosedChannelException();
            return sink.transferFromByteChannel(in, maxCount, true);
        }
