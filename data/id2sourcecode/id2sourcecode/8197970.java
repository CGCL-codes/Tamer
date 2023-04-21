    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        String data = getRequestData();
        int pos = data.lastIndexOf(MARKER);
        if (pos < 0) {
            throw new RuntimeException("Request data have no '" + MARKER + "' instruction");
        }
        SocketChannel sock = (SocketChannel) getSocketChannel();
        if (pos > 0) {
            String constant = data.substring(0, pos);
            ByteBuffer constBuf = ByteBuffer.wrap(constant.getBytes());
            sock.write(constBuf);
        }
        String filename = data.substring(pos + MARKER.length());
        FileInputStream is = new FileInputStream(new File(filename));
        FileChannel source = is.getChannel();
        ByteBuffer sendBuf = ByteBuffer.allocateDirect(1024);
        while (source.read(sendBuf) > 0) {
            sendBuf.flip();
            if (log.isDebugEnabled()) {
                log.debug("Sending " + sendBuf);
            }
            sock.write(sendBuf);
            sendBuf.rewind();
        }
        return readResponse(sock, res);
    }
