    synchronized void receive(int c) throws IOException {
        if (!_connected) {
            throw new IOException("Pipe not connected");
        } else if (_closedByWriter || _closedByReader) {
            throw new IOException("Pipe closed");
        } else if (_readSide != null && !_readSide.isAlive()) {
            throw new IOException("Read end dead");
        }
        _writeSide = Thread.currentThread();
        while (_in == _out) {
            if ((_readSide != null) && !_readSide.isAlive()) {
                throw new IOException("Pipe broken");
            }
            notifyAll();
            try {
                wait(1000);
            } catch (InterruptedException ex) {
                throw new InterruptedIOException();
            }
        }
        if (_in < 0) {
            _in = 0;
            _out = 0;
        }
        _buffer[_in++] = (char) c;
        if (_in >= _buffer.length) {
            _in = 0;
        }
    }
