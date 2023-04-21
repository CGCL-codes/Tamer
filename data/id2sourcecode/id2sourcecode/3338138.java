    public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
        timeTrace("entering onData (pre read by delimiter)");
        ByteBuffer[] buffer = connection.readByteBufferByDelimiter(DELIMITER);
        timeTrace("read by delimiter end (pre write buffer)");
        connection.write(buffer);
        timeTrace("write buffer end (pre write delimiter)");
        connection.write(DELIMITER);
        timeTrace("write delimiter end");
        System.out.print(".");
        return true;
    }
