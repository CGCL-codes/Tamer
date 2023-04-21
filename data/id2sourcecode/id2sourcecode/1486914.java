        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readByteBufferByDelimiter(DELIMITER, Integer.MAX_VALUE));
            return true;
        }
