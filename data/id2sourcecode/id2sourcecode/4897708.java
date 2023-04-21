        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readByteBufferByDelimiter("\r\n") + "\r\n");
            return true;
        }
