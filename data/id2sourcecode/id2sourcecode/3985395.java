    public ChannelBuffer duplicate() {
        ChannelBuffer duplicate = new AggregateChannelBuffer(this);
        duplicate.setIndex(readerIndex(), writerIndex());
        return duplicate;
    }
