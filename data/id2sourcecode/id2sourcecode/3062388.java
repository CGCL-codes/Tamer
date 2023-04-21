    @Override
    public SocketChannel getChannel() {
        SocketChannel poll = _activeChannels.poll();
        if (poll == _dataChannel) {
            _buffer = _dataBuffer;
        } else if (poll == _metaDataChannel) {
            _buffer = _metadataBuffer;
        } else {
            throw new RuntimeException("Unknown channel was activated!");
        }
        return poll;
    }
