    private void check(int length) {
        if (write_pos == write_pos_beg) {
            if (read_pos != read_pos_end) {
                throw new AlertException(AlertProtocol.INTERNAL_ERROR, new SSLHandshakeException("Data was not fully read: " + read_pos + " " + read_pos_end));
            }
            if (write_pos_beg < read_pos_end) {
                write_pos_beg = read_pos_end;
                write_pos = write_pos_beg;
            }
        }
        if (write_pos + length >= buff_size) {
            enlargeBuffer(length);
        }
    }
