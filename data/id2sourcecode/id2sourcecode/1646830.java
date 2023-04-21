    public ChannelBuffer duplicate() {
        ChannelBuffer duplicate = new DeprecatedAggregateChannelBuffer(this);
        duplicate.setIndex(readerIndex(), writerIndex());
        return duplicate;
    }
