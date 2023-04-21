    @Override
    SelectableChannel getChannel() {
        assert isTransportLayerThread();
        return channel_;
    }
