    IRCConnectionListener getChannelMux() {
        if (_mux == null) {
            _mux = new _ChannelMux();
        }
        return _mux;
    }
