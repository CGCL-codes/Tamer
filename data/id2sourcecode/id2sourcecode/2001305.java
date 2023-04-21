    private void forwardRequest(INonBlockingConnection connection, Address address, int length, byte cmd, int keyHashCode) throws IOException {
        LOG.info("forwarding request (cmd=" + cmd + ", hashkey=" + keyHashCode + ") to " + address);
        IBlockingConnection con = connectionPool.getBlockingConnection(address.getAddress().getHostName(), address.getPort());
        con.write(length);
        con.write(cmd);
        con.write(keyHashCode);
        con.write(connection.readByteBufferByLength(length - 1 - 4));
        con.flush();
        int lengthResponse = con.readInt();
        connection.write(lengthResponse);
        connection.write(con.readByteBufferByLength(lengthResponse));
        con.close();
    }
