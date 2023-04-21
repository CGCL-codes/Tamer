        public int read(byte[] buff, int off, int len) throws IOException {
            int read_size = min(len, _write_pos - _read_pos);
            if (read_size > 0) {
                if (read_size >= _window_size) {
                    moveWindow(_write_pos);
                    readFromTempFile(_read_pos, buff, off, read_size);
                } else {
                    int read_low = _read_pos;
                    int read_high = read_low + read_size;
                    if (!contained(read_low, read_high, _window_low, _window_high)) {
                        moveWindow(_read_pos);
                    }
                    System.arraycopy(_memory_buffer, _read_pos - _window_low, buff, off, read_size);
                }
                _read_pos += read_size;
            }
            return read_size;
        }
