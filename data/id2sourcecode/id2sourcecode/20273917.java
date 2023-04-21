    private void updateKey() {
        synchronized (this) {
            int ops = -1;
            if (getChannel().isOpen()) {
                ops = ((_key != null && _key.isValid()) ? _key.interestOps() : -1);
                _interestOps = ((!_dispatched || _readBlocked) ? SelectionKey.OP_READ : 0) | ((!_writable || _writeBlocked) ? SelectionKey.OP_WRITE : 0);
            }
            if (_interestOps == ops && getChannel().isOpen()) return;
        }
        _selectSet.addChange(this);
        _selectSet.wakeup();
    }
