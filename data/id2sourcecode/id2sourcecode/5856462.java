    public void testResponseFetchReply() throws Exception {
        ResponseFetchReply response = new ResponseFetchReply();
        response.asyncKey = 50000;
        response.statusCode = 1000;
        response.responseData = (new ByteArrayBuffer(4096)).putBytes("AAAXXXVVVVDDDD".getBytes()).flip();
        ;
        nbuf buf = nbuf.mallocNative(4096);
        buf.order(ByteOrder.BIG_ENDIAN);
        SocketEndpointReadWriter readWriter = new SocketEndpointReadWriter(0);
        readWriter.writeMessage(buf, response);
        readWriter.cleanup();
        buf.flip();
        byte[] rawdata = new byte[buf.remaining()];
        buf.getBytes(rawdata, 0, rawdata.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(rawdata);
        CommonHeader header = SocketProtocol.read(new DataInputStream(bais));
        assertTrue(header.msgtype == (MSGTYPE_RESPONSE | MSGTYPE_FETCHREPLY));
        buf.free();
    }
