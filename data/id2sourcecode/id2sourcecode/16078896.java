    public void testGWCloseTopreq() {
        RequestGWCloseTopreq request = new RequestGWCloseTopreq();
        request.topreqid = 0X12345678;
        nbuf writebuf = nbuf.mallocNative(4096);
        writebuf.order(ByteOrder.BIG_ENDIAN);
        GatewayReadWriter readWriter = new GatewayReadWriter(0);
        readWriter.writeMessage(writebuf, request);
        writebuf.flip();
        nbuf readbuf = nbuf.mallocNative(4096);
        readbuf.order(ByteOrder.BIG_ENDIAN);
        writebuf.copyTo(0, readbuf, 0, writebuf.remaining());
        readbuf.limit(writebuf.limit());
        CommonHeader header = (CommonHeader) readWriter.readMessage(readbuf);
        readWriter.cleanup();
        assertTrue(header.msgtype == (MSGTYPE_REQUEST | MSGTYPE_GWCLOSETOPREQ));
        assertTrue(((RequestGWCloseTopreq) header).topreqid == request.topreqid);
        readbuf.free();
        writebuf.free();
    }
