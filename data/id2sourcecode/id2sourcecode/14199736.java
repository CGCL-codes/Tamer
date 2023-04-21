        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            connection.setAutoflush(false);
            connection.write(connection.readByteBufferByDelimiter(DELIMITER));
            connection.write(DELIMITER);
            QAUtil.sleep(delay);
            connection.flush();
            return true;
        }
