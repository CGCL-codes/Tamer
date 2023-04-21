    public Node external_read_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1, 2);
        Buffer buffer = new Buffer();
        if (startAt.size() == 2) {
            int sz = (int) startAt.getSubNode(1, Node.TYPE_NUMBER).getNumber();
            byte[] buf = new byte[sz];
            int cread = raf.read(buf);
            if (cread > 0) {
                buffer.write_bytes(buf, cread);
            }
        } else {
            byte[] buf = new byte[4096];
            int cread;
            while ((cread = raf.read(buf)) != -1) {
                if (cread > 0) {
                    buffer.write_bytes(buf, cread);
                }
            }
        }
        External_Buffer res = new External_Buffer();
        res.setBuffer(buffer);
        return ExternalTK.createVObject_External(null, External_Buffer.class.getName(), res);
    }
