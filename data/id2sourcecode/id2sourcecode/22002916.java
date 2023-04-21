        @Override
        public void run() {
            while (!_close) {
                int readData = 0;
                synchronized (_buffer) {
                    while (_buffer.getAvailable() <= 0) {
                        try {
                            _buffer.wait(1000);
                        } catch (final Exception e) {
                        }
                    }
                    if (!_close) {
                        readData = _buffer.get(_block, 0, BLOCKSIZE);
                        _buffer.notify();
                    }
                }
                if (readData > 0 && !_close) {
                    try {
                        _out.write(_block, 0, readData);
                    } catch (final IOException e) {
                        _exception = e;
                        return;
                    }
                }
            }
        }
