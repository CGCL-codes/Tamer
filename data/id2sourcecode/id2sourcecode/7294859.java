    Channel getChannel(String name, String uri) {
        Channel[] candidates;
        Channel channel;
        final int n;
        if ((candidates = m_channelMap.get(name)) != null) {
            int i;
            final int len;
            for (i = 0, len = candidates.length; i < len; i++) {
                channel = candidates[i];
                assert name.equals(channel.name);
                if (uri.equals(channel.uri)) {
                    return channel;
                }
            }
            final Channel[] _candidates;
            _candidates = new Channel[candidates.length + 1];
            System.arraycopy(candidates, 0, _candidates, 0, candidates.length);
            candidates = _candidates;
            n = len;
        } else {
            candidates = new Channel[1];
            n = 0;
        }
        channel = m_channelFactory.createChannel(name, uri, m_totalValueCount, this);
        m_smallChannelList.add(channel);
        candidates[n] = channel;
        m_channelMap.put(name, candidates);
        return channel;
    }
