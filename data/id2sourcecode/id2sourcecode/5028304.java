    public final void flush() throws IOException {
        if (_closed) {
            throw new IOException("writer already closed");
        }
        _out.write(_buffer, 0, _offset);
        _offset = 0;
        _flushed = true;
    }
