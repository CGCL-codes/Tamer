    public boolean dispatch(boolean assumeShortDispatch) throws IOException {
        synchronized (this) {
            if (_key == null) {
                _readBlocked = false;
                _writeBlocked = false;
                this.notifyAll();
                return false;
            }
            if (_readBlocked || _writeBlocked) {
                if (_readBlocked && _key.isReadable()) _readBlocked = false;
                if (_writeBlocked && _key.isWritable()) _writeBlocked = false;
                this.notifyAll();
                _key.interestOps(0);
                return false;
            }
            if (!assumeShortDispatch) _key.interestOps(0);
            if (_dispatched) {
                _key.interestOps(0);
                return false;
            }
            if ((_key.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE && (_key.interestOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                _interestOps = _key.interestOps() & ~SelectionKey.OP_WRITE;
                _key.interestOps(_interestOps);
                _writable = true;
            }
            _dispatched = true;
        }
        return true;
    }
