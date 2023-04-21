    protected boolean tryWrite() throws SinkClosedException {
        try {
            int tryLen;
            if (DEBUG) System.err.println("SockState: tryWrite()");
            if (MAX_WRITE_LEN == -1) {
                tryLen = cur_length_target - cur_offset;
            } else {
                tryLen = Math.min(cur_length_target - cur_offset, MAX_WRITE_LEN);
            }
            if (DEBUG) System.err.println("writing " + tryLen + " bytes");
            byte_buffer.limit(byte_buffer.position() + tryLen);
            cur_offset += nbsock.getChannel().write(byte_buffer);
            if (DEBUG) System.err.println("SockState: tryWrite() of " + tryLen + " bytes (len=" + cur_length_target + ", off=" + cur_offset);
        } catch (IOException ioe) {
            this.close(null);
            throw new SinkClosedException("tryWrite got exception doing write: " + ioe.getMessage());
        }
        if (cur_offset == cur_length_target) {
            if (DEBUG) System.err.println("SockState: tryWrite() completed write of " + cur_length_target + " bytes");
            return true;
        } else return false;
    }
