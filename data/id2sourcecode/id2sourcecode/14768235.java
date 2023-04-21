        public long skip(long bytes) throws IOException {
            if (bytes < 0 || bytes > Integer.MAX_VALUE) throw new IllegalArgumentException();
            int len = (int) bytes;
            if ((len + _read_pos) > _write_pos) len = _write_pos - _read_pos;
            _read_pos += len;
            moveWindow(_write_pos);
            return (long) len;
        }
