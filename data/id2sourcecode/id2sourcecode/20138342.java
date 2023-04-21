    private void append(byte[] src, int from, int length) {
        if (read_pos == read_pos_end) {
            if (write_pos_beg != write_pos) {
                throw new AlertException(AlertProtocol.UNEXPECTED_MESSAGE, new SSLHandshakeException("Handshake message has been received before " + "the last oubound message had been sent."));
            }
            if (read_pos < write_pos) {
                read_pos = write_pos;
                read_pos_end = read_pos;
            }
        }
        if (read_pos_end + length > buff_size) {
            enlargeBuffer(read_pos_end + length - buff_size);
        }
        System.arraycopy(src, from, buffer, read_pos_end, length);
        read_pos_end += length;
    }
