        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            connection.setAutoflush(false);
            connection.write(connection.readByteBufferByDelimiter(DELIMITER, Integer.MAX_VALUE));
            connection.write(DELIMITER);
            connection.flush();
            return true;
        }
