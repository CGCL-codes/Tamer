    protected void doRead() {
        if (DEBUG) System.err.println("SockState: doRead called");
        if (closed) return;
        if (clogged_qel != null) {
            if (DEBUG) System.err.println("SockState: doRead draining clogged element " + clogged_qel);
            try {
                readCompQ.enqueue(clogged_qel);
                clogged_qel = null;
                clogged_numtries = 0;
            } catch (SinkFullException qfe) {
                if ((readClogTries != -1) && (++clogged_numtries >= readClogTries)) {
                    if (DEBUG) System.err.println("SockState: warning: readClogTries exceeded, dropping " + clogged_qel);
                    clogged_qel = null;
                    clogged_numtries = 0;
                } else {
                    return;
                }
            } catch (SinkException sce) {
                this.close(null);
            }
        }
        int len;
        try {
            if (DEBUG) System.err.println("SockState: doRead trying read");
            len = nbsock.getChannel().read(read_byte_buffer);
            if (DEBUG) System.err.println("SockState: read returned " + len);
            if (len == 0) {
                return;
            } else if (len < 0) {
                if (DEBUG) System.err.println("ss.doRead: read failed, sock closed");
                this.close(readCompQ);
                return;
            }
        } catch (Exception e) {
            if (DEBUG) System.err.println("ss.doRead: read got IOException: " + e.getMessage());
            this.close(readCompQ);
            return;
        }
        if (DEBUG) System.err.println("ss.doRead: Pushing up new ATcpInPacket, len=" + len);
        pkt = new ATcpInPacket(conn, readBuf, len, ASocketConst.READ_BUFFER_COPY, seqNum);
        seqNum++;
        if (seqNum == 0) seqNum = 1;
        if (ASocketConst.READ_BUFFER_COPY == false) {
            readBuf = new byte[ASocketConst.READ_BUFFER_SIZE];
            read_byte_buffer = ByteBuffer.wrap(readBuf);
        }
        try {
            readCompQ.enqueue(pkt);
        } catch (SinkFullException qfe) {
            clogged_qel = pkt;
            clogged_numtries = 0;
            return;
        } catch (SinkException sce) {
            this.close(null);
            return;
        }
        read_byte_buffer.rewind();
    }
