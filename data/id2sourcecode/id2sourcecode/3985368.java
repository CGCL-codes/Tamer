    private AggregateChannelBuffer(AggregateChannelBuffer buffer) {
        order = buffer.order;
        slices = buffer.slices.clone();
        indices = buffer.indices.clone();
        setIndex(buffer.readerIndex(), buffer.writerIndex());
    }
