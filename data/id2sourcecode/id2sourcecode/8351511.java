    public Node external_read_chunked_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        Buffer buffer = new Buffer();
        int sz = raf.readInt();
        byte[] buf = new byte[sz];
        int cread = raf.read(buf);
        if (cread > 0) {
            buffer.write_bytes(buf, cread);
        }
        External_Buffer res = new External_Buffer();
        res.setBuffer(buffer);
        return ExternalTK.createVObject_External(null, External_Buffer.class.getName(), res);
    }
