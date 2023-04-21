        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            connection.write(connection.readStringByDelimiter(DELIMITER) + DELIMITER);
            return true;
        }
