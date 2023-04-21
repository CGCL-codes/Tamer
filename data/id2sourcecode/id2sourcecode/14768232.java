        public int read() throws IOException {
            int ret = -1;
            if (!contained(_read_pos, _read_pos + 1, _window_low, _window_high)) {
                moveWindow(_read_pos);
            }
            if (_write_pos > _read_pos) {
                ret = (_memory_buffer[_read_pos - _window_low]) & 0xFF;
                _read_pos++;
            }
            return ret;
        }
