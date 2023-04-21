    @Override
    public void scribe(String value, Scribble scribble, String localName, String uri, int tp) throws IOException {
        final ScriberChannel channel = (ScriberChannel) m_channelKeeper.getChannel(localName, uri);
        final boolean reached = m_channelKeeper.incrementValueCount(channel);
        channel.values.add(new ScriberValueHolder(localName, uri, tp, toValue(value, scribble), this));
        if (reached) ((ChannellingScriber) m_scriber).finishBlock();
    }
