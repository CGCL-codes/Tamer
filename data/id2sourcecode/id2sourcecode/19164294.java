    public void readFully(byte[] buf, int off, int len) {
        checkAvaliable(len);
        int blockAvailable = _currentBlock.available();
        if (blockAvailable > len) {
            _currentBlock.readFully(buf, off, len);
            _current_offset += len;
            return;
        }
        int remaining = len;
        int writePos = off;
        while (remaining > 0) {
            boolean blockIsExpiring = remaining >= blockAvailable;
            int reqSize;
            if (blockIsExpiring) {
                reqSize = blockAvailable;
            } else {
                reqSize = remaining;
            }
            _currentBlock.readFully(buf, writePos, reqSize);
            remaining -= reqSize;
            writePos += reqSize;
            _current_offset += reqSize;
            if (blockIsExpiring) {
                if (_current_offset == _document_size) {
                    if (remaining > 0) {
                        throw new IllegalStateException("reached end of document stream unexpectedly");
                    }
                    _currentBlock = null;
                    break;
                }
                _currentBlock = getDataInputBlock(_current_offset);
                blockAvailable = _currentBlock.available();
            }
        }
    }
