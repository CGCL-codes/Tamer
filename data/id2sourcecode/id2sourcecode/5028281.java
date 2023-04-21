    public final void write(final char[] data, int off, int len) throws IOException {
        if (_closed) {
            throw new IOException("writer already closed");
        }
        while (len > 0) {
            int n = _buffer.length - _offset;
            if (n > 0) {
                if (len < n) {
                    n = len;
                }
                System.arraycopy(data, off, _buffer, _offset, n);
                _offset += n;
                if (n == len) {
                    break;
                }
                off += n;
                len -= n;
            }
            if (autoFlush) {
                flush();
            } else if (bufferSize == UNBOUNDED_BUFFER) {
                int targetLength = _offset + len;
                int newLength = _buffer.length * 2;
                while (newLength < targetLength) {
                    newLength *= 2;
                }
                char[] tmp = new char[newLength];
                System.arraycopy(_buffer, 0, tmp, 0, _offset);
                _buffer = tmp;
            } else {
                throw new IOException("buffer overflowed");
            }
        }
    }
