    public Node external_read_chunked_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        Buffer buffer = new Buffer();
        buffer.setCharset(charset);
        int sz = 0;
        try {
            sz = input.readInt();
        } catch (EOFException eofe) {
            return Node.createNothing();
        }
        if (sz > 0) {
            byte[] buf = new byte[sz];
            int cread = input.read(buf);
            bytePos += cread;
            if (cread > 0) {
                buffer.write_bytes(buf, cread);
            }
            if (buffer.size() == 0 && cread == -1) {
                return Node.createNothing();
            }
        }
        External_Buffer res = new External_Buffer();
        res.setBuffer(buffer);
        return ExternalTK.createVObject_External(null, External_Buffer.class.getName(), res);
    }
