    public Node external_read_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1, 2);
        Buffer buffer = new Buffer();
        buffer.setCharset(charset);
        if (startAt.size() == 2) {
            int sz = (int) startAt.getSubNode(1, Node.TYPE_NUMBER).getNumber();
            byte[] buf = new byte[sz];
            int cread = input.read(buf);
            bytePos += cread;
            if (cread > 0) {
                buffer.write_bytes(buf, cread);
            } else {
                return Node.createNothing();
            }
        } else {
            byte[] buf = new byte[4096];
            int cread;
            while ((cread = input.read(buf)) != -1) {
                bytePos += cread;
                if (cread > 0) {
                    buffer.write_bytes(buf, cread);
                }
            }
            if (buffer.size() == 0 && cread == -1) {
                return Node.createNothing();
            }
        }
        External_Buffer res = new External_Buffer();
        res.setBuffer(buffer);
        return ExternalTK.createVObject_External(null, External_Buffer.class.getName(), res);
    }
