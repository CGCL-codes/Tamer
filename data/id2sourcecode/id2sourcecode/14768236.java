        public int available() throws IOException {
            return _write_pos - _read_pos;
        }
