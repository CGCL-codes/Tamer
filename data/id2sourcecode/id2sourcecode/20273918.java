    void doUpdateKey() {
        synchronized (this) {
            if (getChannel().isOpen()) {
                if (_interestOps > 0) {
                    if (_key == null || !_key.isValid()) {
                        SelectableChannel sc = (SelectableChannel) getChannel();
                        if (sc.isRegistered()) {
                            updateKey();
                        } else {
                            try {
                                _key = ((SelectableChannel) getChannel()).register(_selectSet.getSelector(), _interestOps, this);
                            } catch (Exception e) {
                                Log.ignore(e);
                                if (_key != null && _key.isValid()) _key.cancel();
                                cancelIdle();
                                _manager.endPointClosed(this);
                                _key = null;
                            }
                        }
                    } else _key.interestOps(_interestOps);
                } else {
                    _key.interestOps(0);
                }
            } else {
                if (_key != null && _key.isValid()) _key.cancel();
                cancelIdle();
                _manager.endPointClosed(this);
                _key = null;
            }
        }
    }
