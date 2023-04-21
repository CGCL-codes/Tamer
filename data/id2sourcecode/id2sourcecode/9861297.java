        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readByteBufferByDelimiter(DELIMITER, Integer.MAX_VALUE));
            connection.write(DELIMITER);
            connection.flush();
            return true;
        }
