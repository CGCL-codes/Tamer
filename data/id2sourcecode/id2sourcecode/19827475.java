    protected synchronized boolean addWriteRequest(ASocketRequest req, SelectSourceIF write_selsource) {
        if (closed) return false;
        if (DEBUG) System.err.println("SockState: addWriteRequest called");
        if (this.write_selsource == null) {
            if (DEBUG) System.err.println("SockState: Setting selsource to " + write_selsource);
            if (DEBUG) {
                if (read_selsource == null) {
                    System.err.println("w/r=" + ((NIOSelectSource) write_selsource).getSelector() + "/null");
                } else {
                    System.err.println("w/r=" + ((NIOSelectSource) write_selsource).getSelector() + "/" + ((NIOSelectSource) read_selsource).getSelector());
                }
            }
            if (DEBUG) System.err.println("n_keys = " + ((NIOSelectSource) write_selsource).getSelector().keys().size());
            this.write_selsource = (NIOSelectSource) write_selsource;
            this.write_selsource.setName("WriteSelectSource");
            wselkey = (SelectionKey) write_selsource.register(nbsock.getChannel(), SelectionKey.OP_WRITE);
            if (wselkey == null) {
                System.err.println("SockState: register returned null");
                return false;
            }
            wselkey.attach(this);
            numActiveWriteSockets++;
            if (DEBUG) System.err.println("SockState: Registered with selsource");
        } else if (this.outstanding_writes == 0) {
            numEmptyWrites = 0;
            writeMaskEnable();
        }
        if ((writeClogThreshold != -1) && (this.outstanding_writes > writeClogThreshold)) {
            if (DEBUG) System.err.println("SockState: warning: writeClogThreshold exceeded, dropping " + req);
            if (req instanceof ATcpWriteRequest) return false;
            if (req instanceof ATcpCloseRequest) {
                ATcpCloseRequest creq = (ATcpCloseRequest) req;
                this.close(creq.compQ);
                return true;
            }
        }
        if (DEBUG) System.err.println("SockState: Adding writeReq to tail");
        writeReqList.add_to_tail(req);
        this.outstanding_writes++;
        if (DEBUG) System.err.println("SockState: " + this.outstanding_writes + " outstanding writes");
        return true;
    }
