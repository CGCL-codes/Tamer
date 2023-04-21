    protected byte[] getMessages() {
        int len = (read_pos_end > write_pos) ? read_pos_end : write_pos;
        byte[] res = new byte[len];
        System.arraycopy(buffer, 0, res, 0, len);
        return res;
    }
